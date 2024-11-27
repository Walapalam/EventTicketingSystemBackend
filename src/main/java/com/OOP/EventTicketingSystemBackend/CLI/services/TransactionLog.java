package com.OOP.EventTicketingSystemBackend.CLI.services;

import com.OOP.EventTicketingSystemBackend.CLI.models.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TransactionLog {

    private static TransactionLog instance;
    private List<Transaction> transactionHistory;
    private static AtomicInteger transactionIdCounter = new AtomicInteger(1);

    private TransactionLog() {
        transactionHistory = new ArrayList<>();
    }

    public static synchronized TransactionLog getInstance() {
        if (instance == null) {
            instance = new TransactionLog();
        }
        return instance;
    }

    public static synchronized int generateTransactionId() {
        return transactionIdCounter.incrementAndGet();
    }

    public synchronized void logTransaction(Transaction transaction) {
        transactionHistory.add(transaction);
    }

    public synchronized List<Transaction> getTransactionHistory() {
        return transactionHistory;
    }
}
