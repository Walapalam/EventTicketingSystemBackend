package com.OOP.EventTicketingSystemBackend.CLI.services;

import com.OOP.EventTicketingSystemBackend.CLI.models.Configuration;
import com.OOP.EventTicketingSystemBackend.CLI.models.Event;
import com.OOP.EventTicketingSystemBackend.CLI.models.Ticket;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TicketPool {
    private static TicketPool instance;
    private final BlockingQueue<Ticket> ticketQueue;
    private final Lock lock = new ReentrantLock();

    private TicketPool() {
        // Initialize the queue with the maximum ticket capacity
        this.ticketQueue = new LinkedBlockingQueue<>(Configuration.maxTicketCapacity);
    }

    public static synchronized TicketPool getInstance() {
        if (instance == null) {
            instance = new TicketPool();
        }
        return instance;
    }

    // Method for customers to purchase tickets with LOCKING
    public synchronized boolean purchaseTickets(Event event, int ticketCount) {
        lock.lock();
        try {
            // Check if there are enough tickets available in the event
            if (event.getAvailableTickets() >= ticketCount) {
                for (int i = 0; i < ticketCount; i++) {
                    ticketQueue.take(); // Remove the tickets from the queue
                    event.decrementTickets(1); // Decrement the ticket count in the event
                    Thread.sleep(Configuration.customerRetrievalRate);
                }
                System.out.println(ticketCount + " tickets purchased.");
                return true;
            } else {
                System.out.println("Not enough tickets available for purchase.");
                return false;
            }
        } catch (InterruptedException e) {
            System.out.println("Purchase operation interrupted.");
            return false;
        } finally {
            lock.unlock(); // Release the lock
        }
    }

    // Method for vendors to release tickets with LOCKING
    public synchronized boolean releaseTickets(Event event, int ticketCount) {
        lock.lock();
        try {
            // Check if releasing tickets will not exceed the max capacity
            if (ticketQueue.size() + ticketCount <= Configuration.maxTicketCapacity) {
                for (int i = 0; i < ticketCount; i++) {
                    ticketQueue.put(new Ticket(event.getEventName(), event.getTicketPrice(), event)); // Add tickets to the queue
                    event.setAvailableTickets(event.getAvailableTickets() + 1); // Increment the ticket count in the event
                    Thread.sleep(Configuration.ticketReleaseRate);
                }
                System.out.println(ticketCount + " tickets released.");
                return true;
            } else {
                System.out.println("Cannot release more tickets than the maximum capacity.");
                return false;
            }
        } catch (InterruptedException e) {
            System.out.println("Release operation interrupted.");
            return false;
        } finally {
            lock.unlock(); // Release the lock
        }
    }

    // With SYNCHRONIZATION AND BLOCKING QUEUE
    public synchronized void addTicket(Ticket ticket) throws InterruptedException {
        if (ticketQueue.size() < Configuration.maxTicketCapacity) {
            ticketQueue.put(ticket); // Adds tickets in singular format rather than going for a bulk add from an event
            Thread.sleep(Configuration.ticketReleaseRate);
        } else {
            System.out.println("Ticket pool is at max capacity");
        }
    }

    public synchronized Ticket retrieveTicket() throws InterruptedException {
        if (ticketQueue.isEmpty()){
            System.out.println("Ticket pool is empty");
            return null;
        } else {
            Thread.sleep(Configuration.customerRetrievalRate);
            return ticketQueue.take();
        }
    }

    public int getCurrentPoolSize() {
        return ticketQueue.size();
    }

    // Method to view all available tickets
    public synchronized List<Ticket> viewAllAvailableTickets() {
        return new ArrayList<>(ticketQueue);
    }
}

