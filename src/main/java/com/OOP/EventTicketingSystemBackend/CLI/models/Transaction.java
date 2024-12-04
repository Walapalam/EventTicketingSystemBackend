package com.OOP.EventTicketingSystemBackend.CLI.models;

import java.time.LocalDateTime;

public class Transaction {
    private int transactionId;
    private long userId;  // Could be a vendor or customer ID
    private String transactionType; // "purchase" or "release"
    private long ticketNo;
    private LocalDateTime timestamp;

    private static int transactionCounter = 0;

    public Transaction(long userId, String transactionType, long ticketNo) {
        this.transactionId = setTransactionID();
        this.userId = userId;
        this.transactionType = transactionType;
        this.timestamp = LocalDateTime.now();
        this.ticketNo = ticketNo;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public long getUserId() {
        return userId;
    }

    public int setTransactionID(){
        return ++transactionCounter;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public long getTicketNo() {
        return ticketNo;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId=" + this.transactionId +
                ", userId='" + userId + '\'' +
                ", transactionType='" + transactionType + '\'' +
                ", ticketNo=" + ticketNo +
                ", timestamp=" + timestamp +
                '}';
    }
}

