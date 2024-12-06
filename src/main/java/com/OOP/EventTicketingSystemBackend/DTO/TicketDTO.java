package com.OOP.EventTicketingSystemBackend.DTO;

public class TicketDTO {
    private long ticketId;
    private String eventName;
    private double price;
    private long eventID;

    public TicketDTO(long ticketId, String eventName, double price, long eventID) {
        this.ticketId = ticketId;
        this.eventName = eventName;
        this.price = price;
        this.eventID = eventID;
    }

    public long getTicketId() {
        return ticketId;
    }

    public void setTicketId(long ticketId) {
        this.ticketId = ticketId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public long getEventID() {
        return eventID;
    }

    public void setEventID(long eventID) {
        this.eventID = eventID;
    }
}
