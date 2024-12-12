package com.OOP.EventTicketingSystemBackend.DTO;

import com.OOP.EventTicketingSystemBackend.CLI.models.Transaction;

public class TransactionDTO {
    private long transactionId;
    private long userId;
    private long ticketId;
    private String transactionType;
    private String transactionDate;

    public TransactionDTO(long transactionId, long userId, long ticketId, String transactionType, String transactionDate) {
        this.transactionId = transactionId;
        this.userId = userId;
        this.ticketId = ticketId;
        this.transactionType = transactionType;
        this.transactionDate = transactionDate;
    }

    public TransactionDTO(Transaction transaction) {
        this.transactionId = transaction.getTransactionId();
        this.userId = transaction.getUser().getUserId();
        this.ticketId = transaction.getTicket().getTicketId();
        this.transactionType = transaction.getTransactionType();
        this.transactionDate = transaction.getTimestamp().toString();
    }

    public long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getTicketId() {
        return ticketId;
    }

    public void setTicketId(long ticketId) {
        this.ticketId = ticketId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }
}