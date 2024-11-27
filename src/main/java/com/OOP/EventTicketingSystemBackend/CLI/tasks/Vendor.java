package com.OOP.EventTicketingSystemBackend.CLI.tasks;

import com.OOP.EventTicketingSystemBackend.CLI.models.*;
import com.OOP.EventTicketingSystemBackend.CLI.services.TicketPool;
import com.OOP.EventTicketingSystemBackend.CLI.services.TransactionLog;

import java.util.ArrayList;

public class Vendor extends User implements Runnable{
    private ArrayList<Event> events;

    public Vendor(long userId, String userName, String password, TicketPool ticketPool) {
        super(userId, userName, password);
        events = new ArrayList<Event>();
    }

    public void addEvent(String eventID, String eventName, int availableTickets, double ticketPrice){
        Event event = new Event(eventID, eventName, availableTickets, ticketPrice);
        events.add(event);
    }

    public void addEventTicket(Event event, int ticketCount){
        for (int i = 0; i < ticketCount; i++){
            Ticket ticket = new Ticket(001, event.getEventName(), event.getTicketPrice(), event.getEventID());
            event.getTickets().add(ticket);
            TransactionLog.getInstance().logTransaction(new Transaction(this.getUserId(), "release", ticket.getTicketId()));
        }
    }

    public void releaseTickets(Event event){
        System.out.println("Releasing tickets for event: " + event.getEventName());
        for (Ticket ticket : event.getTickets()){
            releaseTicket(ticket);
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

    public Event getEvent(String eventID){
        for (Event event : events){
            if (event.getEventID().equals(eventID)){
                return event;
            }
        }
        return null;
    }

    @Override
    public void run() {

    }

    @Override
    public String getRole() {
        return "";
    }
}
