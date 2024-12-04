package com.OOP.EventTicketingSystemBackend.CLI.tasks;

import com.OOP.EventTicketingSystemBackend.CLI.models.Event;
import com.OOP.EventTicketingSystemBackend.CLI.models.Ticket;
import com.OOP.EventTicketingSystemBackend.CLI.models.Transaction;
import com.OOP.EventTicketingSystemBackend.CLI.models.User;
import com.OOP.EventTicketingSystemBackend.CLI.services.TicketPool;
import com.OOP.EventTicketingSystemBackend.CLI.services.TransactionLog;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class Customer extends User implements Runnable{
    private ArrayList<Ticket> tickets;
    private Iterator<Ticket> listOfTickets;
    private Scanner scanner = new Scanner(System.in);
    private static long userID = 0;

    public Customer(String userName, String password, String role) {
        super(userName, password, "customer");
        super.role = role;
        this.role = role;
        this.userID = setUserID();
        tickets = new ArrayList<Ticket>();
    }

    public Customer(User user){
        super(user.getUserName(), user.getPassword(), "customer");
        tickets = new ArrayList<Ticket>();
    }



    public static long setUserID() {
        return userID++;
    }

    public synchronized void purchaseTicket(){
        if (TicketPool.getInstance().getCurrentPoolSize() > 0){
            try {
                Ticket ticket = TicketPool.getInstance().retrieveTicket();
                System.out.println("Ticket purchased: " + ticket.getTicketId());
                tickets.add(ticket);
                TransactionLog.getInstance().logTransaction(new Transaction(this.getUserId(), "purchase", ticket.getTicketId()));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Ticket pool is empty");
        }
    }

    public synchronized void purchaseTickets(int count, Event event){
        if (event.getTickets().size() >= count) {
            for (int i = 0; i < count; i++) {
                // Replace with purchaseTicket()
                try {
                    Ticket ticket = TicketPool.getInstance().retrieveTicket();
                    tickets.add(ticket);
                    TransactionLog.getInstance().logTransaction(new Transaction(this.getUserId(), "purchase", ticket.getTicketId()));
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
                    purchaseTicket();
                    break;
                case 2: // Does not work yet
                    System.out.print("Enter the number of tickets to purchase(Does not work): ");
                    int ticketCount = scanner.nextInt();
                    Event dummyEvent = new Event(0001345, "Sample Event", ticketCount, 100.0); // Dummy event
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
