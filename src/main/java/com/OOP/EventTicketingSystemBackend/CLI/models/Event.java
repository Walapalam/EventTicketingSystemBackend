package com.OOP.EventTicketingSystemBackend.CLI.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.apache.tomcat.util.collections.SynchronizedQueue;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private static long eventID = 0;
    private String eventName;
    private int availableTickets;
    private double ticketPrice;
    private BlockingQueue<Ticket> tickets;

    public Event(long eventID, String eventName, int availableTickets, double ticketPrice) {
        this.eventID = eventID;
        this.eventName = eventName;
        this.availableTickets = availableTickets;
        this.ticketPrice = ticketPrice;
    }

    public Event() {

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

    public void addTicket(Ticket ticket) {
        tickets.add(ticket);
    }
}

