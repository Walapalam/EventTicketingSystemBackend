package com.OOP.EventTicketingSystemBackend.CLI.tasks;

import com.OOP.EventTicketingSystemBackend.CLI.models.*;
import com.OOP.EventTicketingSystemBackend.CLI.repositories.EventRepository;
import com.OOP.EventTicketingSystemBackend.CLI.repositories.TicketRepository;
import com.OOP.EventTicketingSystemBackend.CLI.repositories.TransactionRepository;
import com.OOP.EventTicketingSystemBackend.CLI.services.TicketPool;
import com.OOP.EventTicketingSystemBackend.CLI.services.TransactionLog;
import com.OOP.EventTicketingSystemBackend.Controllers.WebSocketHandler;
import com.OOP.EventTicketingSystemBackend.Services.AdminService;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.stream.Collectors;

@Entity
@DiscriminatorValue("vendor")
public class Vendor extends User implements Runnable {

    @Transient
    private static final Logger logger = Logger.getLogger(Vendor.class.getName());

    @Transient
    String scenario = "CLI";

    @Transient
    private AtomicBoolean running;

    @Transient
    private Scanner scanner = new Scanner(System.in);

    @Transient
    private static long userID = 0;

    @Transient
    private EventRepository eventRepository;

    @Transient
    private TicketRepository ticketRepository;

    @Transient
    private TransactionRepository transactionRepository;

    static{
        try {
            // Creating a FileHandler that writes log messages to a file
            FileHandler fileHandler = new FileHandler("vendor.log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            logger.severe("Failed to initialize file handler for logger: " + e.getMessage());
        }
    }

    public Vendor(EventRepository eventRepository, TicketRepository ticketRepository, TransactionRepository transactionRepository) {
        this.eventRepository = eventRepository;
        this.ticketRepository = ticketRepository;
        this.transactionRepository = transactionRepository;
    }

    public Vendor(String userName, String password, TicketPool ticketPool, String role, EventRepository eventRepository, TicketRepository ticketRepository) {
        super(userName, password, "vendor");
        this.role = "vendor";
        this.eventRepository = eventRepository;
        this.ticketRepository = ticketRepository;
    }

    public Vendor() {
    }

    public static long setUserID() {
        return ++userID;
    }

    public void addEvent(String eventName, int availableTickets, double ticketPrice) {
        Event event = new Event(eventName, availableTickets, ticketPrice);
        event.setReleasedBy(this);
        eventRepository.save(event);
        logger.info("Event added: " + event.getEventName());
    }

    public void addEvent(Event event) {
        event.setReleasedBy(this);
        eventRepository.save(event);
        logger.info("Event added: " + event.getEventName());
    }

    public void addEventTicket(Event event, int ticketCount) {
        if (event.getAvailableTickets() + ticketCount <= event.getMaxTickets()) {
            for (int i = 0; i < ticketCount; i++) {
                Ticket ticket = new Ticket(event.getEventName(), event.getTicketPrice(), event);
                try {
                    TicketPool.getInstance().addTicket(ticket);
                    System.out.println(event.getAvailableTickets());
                    event.incrementTickets(1);
                    logger.info("Event updated: " + event.getEventName());
                    eventRepository.save(event);
                    System.out.println("Ticket added: " + ticket.getTicketId());
                    logger.info("Ticket added: " + ticket.getTicketId());
                    transactionRepository.save(new Transaction(this, "release", ticket));
                    ticketRepository.save(ticket);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            eventRepository.save(event);
        } else {
            System.out.println("Cannot add tickets. Exceeds maximum limit.");
            logger.warning("Cannot add tickets. Exceeds maximum limit.");
        }
    }

    public void releaseTickets(Event event, int ticketCount) {
        System.out.println("Releasing tickets for event: " + event.getEventName());
        for (int i = 0; i < ticketCount; i++) {
            Ticket ticket = new Ticket(event.getEventName(), event.getTicketPrice(), event);
            releaseTicket(ticket);
            event.incrementTickets(1);
            eventRepository.save(event);
            ticketRepository.save(ticket);
            System.out.println("Ticket released: " + ticket.getTicketId());
            logger.info("Ticket released: " + ticket.getTicketId());
            transactionRepository.save(new Transaction(this, "release", ticket));
        }

    }

    public void releaseTicket(Ticket ticket) {
        if (TicketPool.getInstance().getCurrentPoolSize() < Configuration.maxTicketCapacity) {
            try {
                TicketPool.getInstance().addTicket(ticket);
                ticketRepository.save(ticket);// Ensure the ticket is saved
                ticket.getEvent().incrementTickets(1);
                eventRepository.save(ticket.getEvent());
                transactionRepository.save(new Transaction(this, "release", ticket));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Ticket pool is at max capacity");
            logger.warning("Ticket pool is at max capacity");
        }
    }

    public Event getEvent(long eventID) {
        return eventRepository.findById((int) eventID)
                .filter(event -> event.getReleasedBy().getUserId() == this.getUserId())
                .orElse(null);
    }

    public void viewEvents() {
        List<Event> events = eventRepository.findAll()
                .stream()
                .filter(event -> event.getReleasedBy().getUserId() == this.getUserId())
                .collect(Collectors.toList());

        for (Event event : events) {
            System.out.println(event);
        }
    }

    public void setScenario(String scenario) {
        this.scenario = scenario;
    }

    @Override
    public void run() throws RuntimeException {
        if (scenario.equals("simulation")){
            while (AdminService.running.get()) {
                System.out.println(Thread.currentThread().getName());
                synchronized (Configuration.class) {
                    if (Configuration.totalTickets >= Configuration.maxTicketCapacity) {
                        System.out.println(Thread.currentThread().getName());
                        WebSocketHandler.broadcast("Simulation ended: Max ticket capacity reached.");
                        logger.info("Simulation ended: Max ticket capacity reached.");
                        Thread.currentThread().interrupt();
                        break;
                    }
                    Event dummyEvent = new Event("Simulated Event", Configuration.maxTicketCapacity, 100.0);
                    dummyEvent.setReleasedBy(this);
                    eventRepository.save(dummyEvent);
                    for (int i = 0; i < Configuration.totalTickets; i++) {
                        Ticket ticket = new Ticket("Simulated Event", 100.0, dummyEvent);
                        releaseTicket(ticket);
                        Configuration.totalTickets++;
                        WebSocketHandler.broadcast("Vendor released ticket: " + ticket.getTicketId());
                        System.out.println("Vendor released ticket: " + ticket.getTicketId());
                        logger.info("Vendor released ticket: " + ticket.getTicketId());
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt(); // Reset the interrupt status
                            break;
                        }
                    }
                }
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    WebSocketHandler.broadcast("Error: Thread interrupted during sleep.");
                    logger.warning("Error: Thread interrupted during sleep.");
                    Thread.currentThread().interrupt(); // Reset the interrupt status
                    break;
                }
            }
        } else {
            boolean vendorMenu = true;
            System.out.println(Thread.currentThread());

            while (vendorMenu) {
                System.out.println("\nVendor Menu:");
                System.out.println("1. Add an Event");
                System.out.println("2. Release Tickets for an Event");
                System.out.println("3. Release Ticket");
                System.out.println("4. View Current Events");
                System.out.println("5. Edit Event");
                System.out.println("6. Logout");
                System.out.print("Choose an option: ");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        System.out.print("Enter Event Name: ");
                        String eventName = scanner.next();
                        System.out.print("Enter Number of Tickets: ");
                        int availableTickets = scanner.nextInt();
                        System.out.print("Enter Ticket Price: ");
                        double ticketPrice = scanner.nextDouble();
                        addEvent(eventName, availableTickets, ticketPrice);
                        break;
                    case 2:
                        viewEvents();
                        System.out.print("Enter Event ID to release tickets: ");
                        long releaseEventId = scanner.nextLong();
                        System.out.println("Enter number of tickets to release: ");
                        int ticketCount = scanner.nextInt();

                        Event event = getEvent(releaseEventId);
                        if (event != null) {
                            releaseTickets(event, ticketCount);
                        } else {
                            System.out.println("Event not found.");
                        }
                        break;
                    case 3:
                        System.out.println("Enter Event name:");
                        String event_Name = scanner.next();

                        System.out.println("Enter Ticket price");
                        double ticket_Price = scanner.nextDouble();

                        Event ticketEvent = new Event(event_Name, 1, ticket_Price);
                        addEvent(ticketEvent);
                        addEventTicket(ticketEvent, 1);
                        break;
                    case 4:
                        System.out.println("Vendor's Events: ");
                        viewEvents();
                        break;
                    case 5:
                        viewEvents();
                        System.out.println("Enter Event ID to edit: ");
                        int editEventId = scanner.nextInt();
                        Event editEvent = eventRepository.findById(editEventId).orElse(null);
                        if (editEvent != null) {
                            System.out.println("Enter new Event Name: ");
                            String newEventName = scanner.next();
                            System.out.println("Enter new Ticket Price: ");
                            double newTicketPrice = scanner.nextDouble();
                            System.out.println("Enter max tickets: ");
                            int maxTickets = scanner.nextInt();

                            editEvent.setMaxTickets(maxTickets);
                            editEvent.setEventName(newEventName);
                            editEvent.setTicketPrice(newTicketPrice);
                            eventRepository.save(editEvent);
                            System.out.println("Event updated successfully.");
                        } else {
                            System.out.println("Event not found.");
                        }
                    case 6:
                        vendorMenu = false;
                        System.out.println("Logged out as Vendor.");
                        Thread.currentThread().interrupt();
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        }

    }

    @Override
    public String getRole() {
        return "vendor";
    }
}