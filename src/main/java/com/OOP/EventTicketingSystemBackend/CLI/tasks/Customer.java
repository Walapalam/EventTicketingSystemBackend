package com.OOP.EventTicketingSystemBackend.CLI.tasks;

import com.OOP.EventTicketingSystemBackend.CLI.models.*;
import com.OOP.EventTicketingSystemBackend.CLI.repositories.EventRepository;
import com.OOP.EventTicketingSystemBackend.CLI.repositories.TicketRepository;
import com.OOP.EventTicketingSystemBackend.CLI.repositories.TransactionRepository;
import com.OOP.EventTicketingSystemBackend.CLI.services.TicketPool;
import com.OOP.EventTicketingSystemBackend.CLI.services.TransactionLog;
import com.OOP.EventTicketingSystemBackend.Controllers.WebSocketHandler;
import com.OOP.EventTicketingSystemBackend.Services.AdminService;
import jakarta.persistence.*;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

@Entity
@DiscriminatorValue("customer")
public class Customer extends User implements Runnable {

    @Transient
    String scenario = "CLI";

    @Transient
    private TransactionRepository transactionRepository;

    @Transient
    private EventRepository eventRepository;

    @Transient
    private TicketRepository ticketRepository;

    @Transient
    private Scanner scanner = new Scanner(System.in);

    @Transient
    private static long userID = 0;

    public Customer(TicketRepository ticketRepository, EventRepository eventRepository, TransactionRepository transactionRepository) {
        this.ticketRepository = ticketRepository;
        this.eventRepository = eventRepository;
        this.transactionRepository = transactionRepository;
    }

    public Customer(String userName, String password, String role, TicketRepository ticketRepository, EventRepository eventRepository) {
        super(userName, password, "customer");
        this.role = role;
        this.eventRepository = eventRepository;
        this.ticketRepository = ticketRepository;
    }

    public Customer() {

    }

    public static long setUserID() {
        return userID++;
    }

    public synchronized void purchaseTicket(Ticket ticket) {
        try {
            if (ticket != null) {
                Event event = ticket.getEvent();
                if (event.decrementTickets(1)) {
                    eventRepository.save(event);
                    ticket.setCustomer(this);
                    ticketRepository.save(ticket);
                    System.out.println("Ticket purchased: " + ticket.getTicketId());

                    transactionRepository.save(new Transaction(this, "purchase", ticket));
                } else {
                    System.out.println("Not enough tickets available.");
                    throw new InterruptedException("Not Enough Tickets");
                }
            } else {
                throw new InterruptedException("Ticket Pool is Empty");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void purchaseTickets(int count, Event event) {
        if (event.getAvailableTickets() >= count) {
            for (int i = 0; i < count; i++) {
                try {
                    Ticket ticket = TicketPool.getInstance().retrieveTicket();
                    if (ticket != null) {
                        ticket.setCustomer(this);
                        ticketRepository.save(ticket);
                        event.decrementTickets(1);
                        eventRepository.save(event);
                        transactionRepository.save(new Transaction(this, "purchase", ticket));
                        System.out.println("Ticket purchased: " + ticket.getTicketId());
                    } else {
                        System.out.println("Ticket pool is empty.");
                        break;
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            System.out.println("Not enough tickets available for purchase.");
        }
    }

    public void setScenario(String scenario) {
        this.scenario = scenario;
    }

    @Override
    public void run() throws RuntimeException {
        if (scenario.equals("simulation")){
            while (AdminService.running.get()) {
                try {
                    System.out.println("Goes to purchase ticket");
                    Event dummyEvent = new Event("Dummy Event", 10, 10.0);
                    purchaseTickets(1, dummyEvent);
                    Thread.sleep(3311);
                } catch (InterruptedException e) {
                    WebSocketHandler.broadcast("Error: Thread interrupted during ticket purchase.");
                    Thread.currentThread().interrupt(); // Reset the interrupt status
                    break;
                } catch (RuntimeException e) {
                    WebSocketHandler.broadcast("Error: " + e.getMessage());
                    Thread.currentThread().interrupt(); // Reset the interrupt status
                    break;
                }
            }
        } else {
            boolean customerMenu = true;

            while (customerMenu) {
                System.out.println("\nCustomer Menu:");
                System.out.println("1. Purchase a Ticket");
                System.out.println("2. Purchase Multiple Tickets");
                System.out.println("3. View Current Tickets");
                System.out.println("4. Logout");
                System.out.print("Choose an option: ");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        TicketPool.getInstance().viewAllAvailableTickets();
                        System.out.println("Enter Event ID: ");
                        long eventID = scanner.nextLong();
                        purchaseTickets(1, eventRepository.findById((int) eventID).orElse(null));
                        break;
                    case 2:
                        TicketPool.getInstance().viewAllAvailableTickets();
                        System.out.print("Enter the number of tickets to purchase: ");
                        int ticketCount = scanner.nextInt();
                        System.out.print("Enter Event ID: ");
                        long eventId = scanner.nextLong();
                        Event event = eventRepository.findById((int) eventId).orElse(null);
                        if (event != null) {
                            purchaseTickets(ticketCount, event);
                        } else {
                            System.out.println("Event not found.");
                        }
                        break;
                    case 3:
                        System.out.println("Customer's Tickets: ");
                        seeTickets();
                        break;
                    case 4:
                        customerMenu = false;
                        System.out.println("Logged out as Customer.");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        }

    }

    public void seeTickets() {
        TicketPool.getInstance().viewAllAvailableTickets();
    }

    @Override
    public String getRole() {
        return "customer";
    }
}