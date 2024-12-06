package com.OOP.EventTicketingSystemBackend.CLI.models;

import com.OOP.EventTicketingSystemBackend.CLI.services.TicketPool;
import jakarta.persistence.*;
import org.apache.tomcat.util.collections.SynchronizedQueue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long eventID;

    private String eventName;
    private int availableTickets;
    private double ticketPrice;

    @Transient
    private BlockingQueue<Ticket> tickets;

    @Transient
    private static long eventIDCounter = 0;

    public Event(String eventName, int availableTickets, double ticketPrice) {
        this.eventID = generateEventID();
        this.eventName = eventName;
        this.availableTickets = availableTickets;
        this.ticketPrice = ticketPrice;
        this.tickets = new LinkedBlockingQueue<Ticket>(availableTickets);
    }

    public Event() {

    }

    public static long generateEventID() {
        return ++eventIDCounter;
    }

    public long getEventID() {
        return eventID;
    }

    public void setEventID(long eventID) {
        this.eventID = eventID;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public int getAvailableTickets() {
        return availableTickets;
    }

    public void setAvailableTickets(int availableTickets) {
        this.availableTickets = availableTickets;
    }

    public BlockingQueue<Ticket> getTickets() {
        return tickets;
    }

    @Override
    public String toString() {
        return "Event [ID=" + this.eventID + ", Name=" + eventName +
                ", Available Tickets=" + availableTickets +
                ", Ticket Price=" + ticketPrice + "]";
    }

    public synchronized void addTicket(Ticket ticket) {
        tickets.add(ticket);
        try {
            TicketPool.getInstance().addTicket(ticket);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

