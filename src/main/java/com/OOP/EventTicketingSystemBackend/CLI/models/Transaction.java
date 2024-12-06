package com.OOP.EventTicketingSystemBackend.CLI.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int transactionId;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    //private long userId;  // Could be a vendor or customer ID
    private String transactionType;// "purchase" or "release"

    @ManyToOne
    @JoinColumn(name = "ticketId", nullable = false)
    private Ticket ticket;
    //private long ticketNo;
    private LocalDateTime timestamp;

    @Transient
    private static int transactionCounter = 0;

    public Transaction(User user, String transactionType, Ticket ticket) {
        this.transactionId = setTransactionID();

        //this.userId = userId;
        this.user = user;

        this.transactionType = transactionType;
        this.timestamp = LocalDateTime.now();

        //this.ticketNo = ticketNo;
        this.ticket = ticket;
    }

    public Transaction() {

    }

    public int getTransactionId() {
        return transactionId;
    }

    public long getUserId() {
        return user.getUserId();
    }

    public int setTransactionID(){
        return ++transactionCounter;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public long getTicketNo() {
        return ticket.getTicketId();
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId=" + this.transactionId +
                ", userId='" + user.getUserId() + '\'' +
                ", transactionType='" + transactionType + '\'' +
                ", ticketNo=" + ticket.getTicketId() +
                ", timestamp=" + timestamp +
                '}';
    }
}

