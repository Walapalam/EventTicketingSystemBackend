package com.OOP.EventTicketingSystemBackend.DTO;

import com.OOP.EventTicketingSystemBackend.CLI.models.Ticket;

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

    public TicketDTO(Ticket ticket){
        this.ticketId = ticket.getTicketId();
        this.eventName = ticket.getEvent().getEventName();
        this.price = ticket.getPrice();
        this.eventID = ticket.getEvent().getEventID();
    }

    public TicketDTO(long ticketId, String eventName, double price) {
        this.ticketId = ticketId;
        this.eventName = eventName;
        this.price = price;
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
