package com.OOP.EventTicketingSystemBackend.DTO;

import com.OOP.EventTicketingSystemBackend.CLI.models.Event;

public class EventDTO {
    private long eventID;
    private String eventName;
    private int availableTickets;
    private double ticketPrice;

    public EventDTO(long eventID, String eventName, int availableTickets, double ticketPrice) {
        this.eventID = eventID;
        this.eventName = eventName;
        this.availableTickets = availableTickets;
        this.ticketPrice = ticketPrice;
    }

    public EventDTO(Event event){
        this.eventID = event.getEventID();
        this.eventName = event.getEventName();
        this.availableTickets = event.getAvailableTickets();
        this.ticketPrice = event.getTicketPrice();
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

    public int getAvailableTickets() {
        return availableTickets;
    }

    public void setAvailableTickets(int availableTickets) {
        this.availableTickets = availableTickets;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }
}
