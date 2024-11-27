package com.OOP.EventTicketingSystemBackend.CLI.models;

public class Ticket {
    private long ticketId;
    private String eventName;
    private double price;
    private String eventID;

    // For single tickets
    public Ticket(int ticketId, String eventName, double price) {
        this.ticketId = ticketId;
        this.eventName = eventName;
        this.price = price;
    }

    // For multiple tickets in events
    public Ticket(int ticketId, String eventName, double price, String eventID) {
        this.ticketId = ticketId;
        this.eventName = eventName;
        this.price = price;
        this.eventID = eventID;
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

    public String getEventID() {
        return eventID;
    }
}
