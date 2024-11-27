package com.OOP.EventTicketingSystemBackend.CLI.tasks;

import com.OOP.EventTicketingSystemBackend.CLI.models.Event;
import com.OOP.EventTicketingSystemBackend.CLI.models.Ticket;
import com.OOP.EventTicketingSystemBackend.CLI.models.Transaction;
import com.OOP.EventTicketingSystemBackend.CLI.models.User;
import com.OOP.EventTicketingSystemBackend.CLI.services.TicketPool;
import com.OOP.EventTicketingSystemBackend.CLI.services.TransactionLog;

import java.util.ArrayList;

public class Customer extends User implements Runnable{
    private ArrayList<Ticket> tickets;


    public Customer(long userId, String userName, String password) {
        super(userId, userName, password);
        tickets = new ArrayList<Ticket>();
    }

    public synchronized void purchaseTicket(){
        if (TicketPool.getInstance().getCurrentPoolSize() > 0){
            try {
                Ticket ticket = TicketPool.getInstance().retrieveTicket();
                tickets.add(ticket);
                TransactionLog.getInstance().logTransaction(new Transaction(this.getUserId(), "purchase", ticket.getTicketId()));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Ticket pool is empty");
        }
    }

    public synchronized void purchaseTickets(int count, Event event){
        if (event.getTickets().size() >= count) {
            for (int i = 0; i < count; i++) {
                // Replace with purchaseTicket()
                try {
                    Ticket ticket = TicketPool.getInstance().retrieveTicket();
                    tickets.add(ticket);
                    TransactionLog.getInstance().logTransaction(new Transaction(this.getUserId(), "purchase", ticket.getTicketId()));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            System.out.println("Not enough tickets available for purchase.");
        }
    }

    @Override
    public void run() {

    }

    @Override
    public String getRole() {
        return "Customer";
    }

    @Override
    public String toString() {
        return "Customer{" +
                "userId=" + getUserId() +
                ", userName='" + getUserName() + '\'' +
                '}';
    }
}
