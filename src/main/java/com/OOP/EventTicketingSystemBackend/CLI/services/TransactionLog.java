package com.OOP.EventTicketingSystemBackend.CLI.services;

import com.OOP.EventTicketingSystemBackend.CLI.models.Transaction;
import com.OOP.EventTicketingSystemBackend.CLI.repositories.EventRepository;
import com.OOP.EventTicketingSystemBackend.CLI.repositories.TransactionRepository;
import org.springframework.cglib.core.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class TransactionLog {
    private static TransactionLog instance;
    private BlockingQueue<Transaction> transactionHistory;

    private TransactionLog() {
        transactionHistory = new LinkedBlockingQueue<Transaction>();
    }

    public static synchronized TransactionLog getInstance() {
        if (instance == null) {
            instance = new TransactionLog();
        }
        return instance;
    }

    public static void initializeTransactionLog(TransactionRepository transactionRepository) {
        List<Transaction> transactions = transactionRepository.findAll();
        for (Transaction transaction : transactions) {
            instance.logTransaction(transaction);
        }
    }

    public void logTransaction(Transaction transaction) {
        transactionHistory.add(transaction);
    }

    public void getTransactionHistory() {
        for (Transaction transaction : transactionHistory) {
            System.out.println(transaction);
        }
    }
}
