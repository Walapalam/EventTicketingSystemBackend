package com.OOP.EventTicketingSystemBackend.CLI.models;

import com.OOP.EventTicketingSystemBackend.CLI.tasks.Customer;
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
        //this.userId = userId;
        this.user = user;

        this.transactionType = transactionType;
        this.timestamp = LocalDateTime.now();

        //this.ticketNo = ticketNo;
        this.ticket = ticket;
    }

    public Transaction() {

    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public static int getTransactionCounter() {
        return transactionCounter;
    }

    public static void setTransactionCounter(int transactionCounter) {
        Transaction.transactionCounter = transactionCounter;
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

    public Ticket getTicket() {
        return ticket;
    }

    public User getUser() {
        return user;
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

    public void setUser(Customer customer) {
        this.user = customer;
    }
}

