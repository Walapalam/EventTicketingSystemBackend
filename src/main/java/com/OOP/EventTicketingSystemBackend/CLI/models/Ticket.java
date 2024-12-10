package com.OOP.EventTicketingSystemBackend.CLI.models;

import jakarta.persistence.*;

@Entity
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long ticketId;

    private String eventName;
    private double price;

    @ManyToOne
    @JoinColumn(name = "eventID", nullable = true)
    private Event event;

    @ManyToOne
    @JoinColumn(name = "customerID", nullable = true)
    private User customer;

    @Transient
    private static long ticketIDCounter = 0;

    // For single tickets
    public Ticket(String eventName, double price) {
        this.ticketId = generateTicketID();
        this.eventName = eventName;
        this.price = price;
    }

    // For multiple tickets in events
    public Ticket(String eventName, double price, Event event) {
        this.ticketId = generateTicketID();
        this.eventName = eventName;
        this.price = price;
        //this.eventID = eventID;
        this.event = event;

    }

    public Ticket() {

    }

    public static long generateTicketID() {
        return ++ticketIDCounter;
    }
    public long getTicketId() {
        return ticketId;
    }

    public String getEventName() {
        return eventName;
    }

    public double getPrice() {
        return price;
    }

    public long getEventID() {
        return event.getEventID();
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setEventID(long eventID) {
        event.setEventID(eventID);
    }

    public void setTicketId(long ticketId) {
        this.ticketId = ticketId;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Event getEvent() {
        return event;
    }
}
