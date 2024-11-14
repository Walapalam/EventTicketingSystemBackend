package com.OOP.EventTicketingSystemBackend.CLI;

public class Ticket {
    private String ticketNo;
    private String eventName;
    private boolean isAvailable;
    private double ticketPrice;

    public Ticket(String ticketNo, String eventName, boolean isAvailable, double ticketPrice) {
        this.ticketNo = ticketNo;
        this.eventName = eventName;
        this.isAvailable = isAvailable;
        this.ticketPrice = ticketPrice;
    }

    public String getEventName() {
        return eventName;
    }

    public String getTicketNo() {
        return ticketNo;
    }

    public boolean getIsAvailable() {
        return isAvailable;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setTicketNo(String ticketNo) {
        this.ticketNo = ticketNo;
    }

    public void setIsAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public void setTicketPrice(double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public String toString() {
        return "Ticket No: " + ticketNo + ", Event Name: " + eventName + ", Available: " + isAvailable;
    }
}
