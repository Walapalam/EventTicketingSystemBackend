package com.OOP.EventTicketingSystemBackend.CLI.tasks;

import com.OOP.EventTicketingSystemBackend.CLI.models.*;
import com.OOP.EventTicketingSystemBackend.CLI.services.TicketPool;
import com.OOP.EventTicketingSystemBackend.CLI.services.TransactionLog;

import java.util.ArrayList;
import java.util.Scanner;

public class Vendor extends User implements Runnable{
    private ArrayList<Event> events;
    private Scanner scanner = new Scanner(System.in);
    private static long userID = 0;

    public Vendor(String userName, String password, TicketPool ticketPool, String role) {
        super(userName, password, "vendor");
        this.role = "vendor";
        this.userID = setUserID();
        events = new ArrayList<Event>();
    }

    public void addEvent(long eventID, String eventName, int availableTickets, double ticketPrice){
        Event event = new Event(eventID, eventName, availableTickets, ticketPrice);
        events.add(event);
    }

    public static long setUserID(){
        return ++userID;
    }
    public void addEventTicket(Event event, int ticketCount){
        for (int i = 0; i < ticketCount; i++){
            Ticket ticket = new Ticket(event.getEventName(), event.getTicketPrice(), event.getEventID());
            event.getTickets().add(ticket);
            TransactionLog.getInstance().logTransaction(new Transaction(this.getUserId(), "release", ticket.getTicketId()));
        }
    }

    public void releaseTickets(Event event){
        System.out.println("Releasing tickets for event: " + event.getEventName());
        for (Ticket ticket : event.getTickets()){
            releaseTicket(ticket);
            System.out.println("Ticket released: " + ticket.getTicketId());
            TransactionLog.getInstance().logTransaction(new Transaction(this.getUserId(), "release", ticket.getTicketId()));
        }
    }

    public void releaseTicket(Ticket ticket){
        if (TicketPool.getInstance().getCurrentPoolSize() < Configuration.maxTicketCapacity){
            try {
                TicketPool.getInstance().addTicket(ticket);
                TransactionLog.getInstance().logTransaction(new Transaction(this.getUserId(), "release", ticket.getTicketId()));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Ticket pool is at max capacity");
        }
    }

    public Event getEvent(long eventID){
        for (Event event : events){
            if (event.getEventID() == eventID){
                return event;
            }
        }
        return null;
    }

    public void viewEvents(){
        for (Event event : events){
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
                    System.out.print("Enter Event ID: ");
                    long eventId = scanner.nextLong();
                    System.out.print("Enter Event Name: ");
                    String eventName = scanner.next();
                    System.out.print("Enter Number of Tickets: ");
                    int availableTickets = scanner.nextInt();
                    System.out.print("Enter Ticket Price: ");
                    double ticketPrice = scanner.nextDouble();
                    addEvent(eventId, eventName, availableTickets, ticketPrice);
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
                    Ticket ticket = new Ticket("Sample Event", 100.0, 00001); // Dummy ticket
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
