package com.OOP.EventTicketingSystemBackend.CLI.models;

public class Ticket {
    private long ticketId;
    private String eventName;
    private double price;
    private long eventID;
    private static long ticketIDCounter = 0;

    // For single tickets
    public Ticket(String eventName, double price) {
        this.ticketId = generateTicketID();
        this.eventName = eventName;
        this.price = price;
    }

    // For multiple tickets in events
    public Ticket(String eventName, double price, long eventID) {
        this.ticketId = generateTicketID();
        this.eventName = eventName;
        this.price = price;
        this.eventID = eventID;
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
        return eventID;
    }
}
