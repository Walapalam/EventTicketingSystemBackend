package com.OOP.EventTicketingSystemBackend.CLI.models;

import com.OOP.EventTicketingSystemBackend.CLI.services.TicketPool;
import jakarta.persistence.*;
import java.util.concurrent.atomic.AtomicInteger;

@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long eventID;

    private String eventName;
    private double ticketPrice;
    private AtomicInteger availableTickets;
    private int maxTickets;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User releasedBy;

    @Transient
    private static long eventIDCounter = 0;

    public Event(String eventName, int maxTickets, double ticketPrice) {
        this.eventName = eventName;
        this.maxTickets = maxTickets;
        this.availableTickets = new AtomicInteger(0);
        this.ticketPrice = ticketPrice;
    }

    public Event() {
        this.availableTickets = new AtomicInteger(0);
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
        return availableTickets.get();
    }

    public void setAvailableTickets(int availableTickets) {
        this.availableTickets.set(availableTickets);
    }

    public boolean decrementTickets(int count) {
        return availableTickets.addAndGet(-count) >= 0;
    }

    public void setReleasedBy(User releasedBy) {
        this.releasedBy = releasedBy;
    }

    public User getReleasedBy() {
        return releasedBy;
    }

    public int getMaxTickets() {
        return maxTickets;
    }

    @Override
    public String toString() {
        return "Event [ID=" + this.eventID + ", Name=" + eventName +
                ", Available Tickets=" + availableTickets +
                ", Ticket Price=" + ticketPrice + "]";
    }
}