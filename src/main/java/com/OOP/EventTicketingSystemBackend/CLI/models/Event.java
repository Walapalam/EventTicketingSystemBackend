package com.OOP.EventTicketingSystemBackend.CLI.models;

import jakarta.persistence.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long eventID;

    private String eventName;
    private double ticketPrice;
    private int maxTickets;
    private int availableTickets;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User releasedBy;

    @Transient
    private static long eventIDCounter = 0;

    @Transient
    private final Lock lock = new ReentrantLock();

    public Event(String eventName, int maxTickets, double ticketPrice) {
        this.eventName = eventName;
        this.maxTickets = maxTickets;
        this.ticketPrice = ticketPrice;
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
        lock.lock();
        try {
            return availableTickets;
        } finally {
            lock.unlock();
        }
    }

    public void setAvailableTickets(int availableTickets) {
        lock.lock();
        try {
            this.availableTickets = availableTickets;
        } finally {
            lock.unlock();
        }
    }

    public boolean decrementTickets(int count) {
        lock.lock();
        try {
            if (availableTickets - count < 0) {
                return false;
            }
            availableTickets -= count;
            return true;
        } finally {
            lock.unlock();
        }
    }

    public void incrementTickets(int count) {
        lock.lock();
        try {
            availableTickets += count;
        } finally {
            lock.unlock();
        }
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
                ", Ticket Price=" + ticketPrice
                + ", Released By=" + releasedBy.getUserId()
                + ", Max Tickets=" + maxTickets + "]";
    }

    public void setMaxTickets(int maxTickets) {
        this.maxTickets = maxTickets;
    }
}