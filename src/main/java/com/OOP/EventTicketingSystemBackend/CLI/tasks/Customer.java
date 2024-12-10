package com.OOP.EventTicketingSystemBackend.CLI.tasks;

import com.OOP.EventTicketingSystemBackend.CLI.models.Event;
import com.OOP.EventTicketingSystemBackend.CLI.models.Ticket;
import com.OOP.EventTicketingSystemBackend.CLI.models.Transaction;
import com.OOP.EventTicketingSystemBackend.CLI.models.User;
import com.OOP.EventTicketingSystemBackend.CLI.repositories.TicketRepository;
import com.OOP.EventTicketingSystemBackend.CLI.services.TicketPool;
import com.OOP.EventTicketingSystemBackend.CLI.services.TransactionLog;
import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Entity
@DiscriminatorValue("customer")
@Component
public class Customer extends User implements Runnable{
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ticket> tickets;

    @Transient
    private Iterator<Ticket> listOfTickets;

    @Transient
    private Scanner scanner = new Scanner(System.in);
    private static long userID = 0;

    @Transient
    private TicketRepository ticketRepository;

    public Customer(String userName, String password, String role) {
        super(userName, password, "customer");
        super.role = role;
        this.role = role;
        this.userID = setUserID();
        tickets = new ArrayList<Ticket>();
    }

    public Customer(User user){
        super(user.getUsername(), user.getPassword(), "customer");
        tickets = new ArrayList<Ticket>();
    }

    public Customer() {

    }

    @Autowired
    public Customer(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }


    public static long setUserID() {
        return userID++;
    }

    public synchronized void purchaseTicket(int ticketID) {
        Optional<Ticket> ticketOptional = ticketRepository.findById(ticketID);
        if (ticketOptional.isPresent()) {
            Ticket ticket = ticketOptional.get();
            try {
                if (TicketPool.getInstance().getCurrentPoolSize() > 0 && TicketPool.getInstance().retrieveTicket().equals(ticket)) {
                    System.out.println("Ticket purchased: " + ticket.getTicketId());
                    tickets.add(ticket);
                    TransactionLog.getInstance().logTransaction(new Transaction(this, "purchase", ticket));
                } else {
                    System.out.println("Ticket pool is empty or ticket not available.");
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("Ticket not found in repository.");
        }
    }

    public synchronized void purchaseTickets(int count, Event event) {
        if (event.getAvailableTickets() >= count) {
            for (int i = 0; i < count; i++) {
                try {
                    Ticket ticket = TicketPool.getInstance().retrieveTicket();
                    if (ticket != null) {
                        tickets.add(ticket);
                        event.decrementTickets(1);
                        TransactionLog.getInstance().logTransaction(new Transaction(this, "purchase", ticket));
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

    @Override
    public void run() {
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
                    System.out.println("Enter the ticket ID to purchase: ");
                    int ticketID = scanner.nextInt();
                    purchaseTicket(ticketID);
                    break;
                case 2: // Does not work yet
                    System.out.print("Enter the number of tickets to purchase(Does not work): ");
                    int ticketCount = scanner.nextInt();
                    Event dummyEvent = new Event("Sample Event", ticketCount, 100.0); // Dummy event
                    purchaseTickets(ticketCount, dummyEvent);
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

    public void seeTickets(){
        listOfTickets = tickets.iterator();

        while (listOfTickets.hasNext()){
            System.out.println(listOfTickets.next());
        }
    }

    @Override
    public String getRole() {
        return "customer";
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
