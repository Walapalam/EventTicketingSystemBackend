package com.OOP.EventTicketingSystemBackend.CLI.tasks;

import com.OOP.EventTicketingSystemBackend.CLI.models.*;
import com.OOP.EventTicketingSystemBackend.CLI.repositories.EventRepository;
import com.OOP.EventTicketingSystemBackend.CLI.repositories.TicketRepository;
import com.OOP.EventTicketingSystemBackend.CLI.services.TicketPool;
import com.OOP.EventTicketingSystemBackend.CLI.services.TransactionLog;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

@Entity
@DiscriminatorValue("vendor")
@Component
public class Vendor extends User implements Runnable {
    @Transient
    private Scanner scanner = new Scanner(System.in);
    private static long userID = 0;

    @Transient
    private EventRepository eventRepository;

    @Transient
    private TicketRepository ticketRepository;

    @Autowired
    public Vendor(EventRepository eventRepository, TicketRepository ticketRepository) {
        this.eventRepository = eventRepository;
        this.ticketRepository = ticketRepository;
    }

    public Vendor(String userName, String password, TicketPool ticketPool, String role) {
        super(userName, password, "vendor");
        this.role = "vendor";
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
    }

    public void addEvent(Event event) {
        event.setReleasedBy(this);
        eventRepository.save(event);
    }

    public void addEventTicket(Event event, int ticketCount) {
        if (event.getAvailableTickets() + ticketCount <= event.getMaxTickets()) {
            for (int i = 0; i < ticketCount; i++) {
                Ticket ticket = new Ticket(event.getEventName(), event.getTicketPrice(), event);
                try {
                    TicketPool.getInstance().addTicket(ticket);
                    event.setAvailableTickets(event.getAvailableTickets() + 1);
                    System.out.println("Ticket added: " + ticket.getTicketId());
                    TransactionLog.getInstance().logTransaction(new Transaction(this, "release", ticket));
                    ticketRepository.save(ticket);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            eventRepository.save(event);
        } else {
            System.out.println("Cannot add tickets. Exceeds maximum limit.");
        }
    }

    public void releaseTickets(Event event) {
        System.out.println("Releasing tickets for event: " + event.getEventName());
        for (int i = 0; i < event.getAvailableTickets(); i++) {
            Ticket ticket = new Ticket(event.getEventName(), event.getTicketPrice(), event);
            releaseTicket(ticket);
            ticketRepository.save(ticket);
            System.out.println("Ticket released: " + ticket.getTicketId());
            TransactionLog.getInstance().logTransaction(new Transaction(this, "release", ticket));
        }
    }

    public void releaseTicket(Ticket ticket) {
        if (TicketPool.getInstance().getCurrentPoolSize() < Configuration.maxTicketCapacity) {
            try {
                TicketPool.getInstance().addTicket(ticket);
                ticketRepository.save(ticket);
                TransactionLog.getInstance().logTransaction(new Transaction(this, "release", ticket));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Ticket pool is at max capacity");
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

    @Override
    public void run() {
        boolean vendorMenu = true;
        System.out.println(Thread.currentThread());

        while (vendorMenu) {
            System.out.println("\nVendor Menu:");
            System.out.println("1. Add an Event");
            System.out.println("2. Release Tickets for an Event");
            System.out.println("3. Release Ticket");
            System.out.println("4. View Current Events");
            System.out.println("5. Logout");
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
                    System.out.print("Enter Event ID to release tickets: ");
                    long releaseEventId = scanner.nextLong();
                    Event event = getEvent(releaseEventId);
                    if (event != null) {
                        releaseTickets(event);
                    } else {
                        System.out.println("Event not found.");
                    }
                    break;
                case 3:
                    Event ticketEvent = new Event("Movie", 1, 2000);
                    addEvent(ticketEvent);

                    addEventTicket(ticketEvent, 1);
                    Ticket ticket = new Ticket("Default Event", 100.0, ticketEvent); // Dummy ticket

                    releaseTickets(ticketEvent);
                    releaseTicket(ticket);
                    break;
                case 4:
                    System.out.println("Vendor's Events: ");
                    viewEvents();
                    break;
                case 5:
                    vendorMenu = false;
                    System.out.println("Logged out as Vendor.");
                    Thread.currentThread().interrupt();
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    @Override
    public String getRole() {
        return "vendor";
    }
}