package com.OOP.EventTicketingSystemBackend.CLI.models;

import java.util.ArrayList;

public class Event {
    private String eventID;
    private String eventName;
    private int availableTickets;
    private double ticketPrice;
    private ArrayList<Ticket> tickets;

    public Event(String eventID, String eventName, int availableTickets, double ticketPrice) {
        this.eventID = eventID;
        this.eventName = eventName;
        this.availableTickets = availableTickets;
        this.ticketPrice = ticketPrice;
        this.tickets = new ArrayList<Ticket>();
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
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

    public ArrayList<Ticket> getTickets() {
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

