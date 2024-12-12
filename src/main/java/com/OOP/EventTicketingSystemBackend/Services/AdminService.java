package com.OOP.EventTicketingSystemBackend.Services;

import com.OOP.EventTicketingSystemBackend.CLI.EventTicketingCLI;
import com.OOP.EventTicketingSystemBackend.CLI.models.User;
import com.OOP.EventTicketingSystemBackend.CLI.services.TicketPool;
import com.OOP.EventTicketingSystemBackend.CLI.tasks.Customer;
import com.OOP.EventTicketingSystemBackend.CLI.tasks.Vendor;
import com.OOP.EventTicketingSystemBackend.DTO.*;
import com.OOP.EventTicketingSystemBackend.CLI.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.OOP.EventTicketingSystemBackend.Controllers.WebSocketHandler.broadcast;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public static AtomicBoolean running = new AtomicBoolean(false);
    private Thread vendorThread;
    private Thread customerThread;

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> new UserDTO(user))
                .toList();
    }

    public List<TicketDTO> getAllTickets() {
        return ticketRepository.findAll().stream()
                .map(ticket -> new TicketDTO(ticket))
                .toList();
    }

    public List<EventDTO> getAllEvents() {
        return eventRepository.findAll().stream()
                .map(event -> new EventDTO(event))
                .toList();
    }

    public List<TransactionDTO> getAllTransactions() {
        return transactionRepository.findAll().stream()
                .map(transaction -> new TransactionDTO(transaction))
                .toList();
    }

    public void startSimulation() {
        running.set(true);

        User user1 = userRepository.findByUsername("usernamee");
        User user2 = userRepository.findByUsername("username");

        Customer customer = new Customer(ticketRepository, eventRepository, transactionRepository);
        customer.setUsername(user1.getUsername());
        customer.setPassword(user1.getPassword());
        customer.setRole(user1.getRole());
        customer.setUserId(user1.getUserId());
        customer.setScenario("simulation");
        Vendor vendor = new Vendor(eventRepository, ticketRepository, transactionRepository);
        vendor.setUsername(user2.getUsername());
        vendor.setPassword(user2.getPassword());
        vendor.setRole(user2.getRole());
        vendor.setUserId(user2.getUserId());
        vendor.setScenario("simulation");

        vendorThread = new Thread(vendor);
        customerThread = new Thread(customer);

        try{
            vendorThread.start();
            customerThread.start();
        } catch (RuntimeException e){
            broadcast("Simulation stopped due to request");
        }


    }

    public void stopSimulation(String message) {
        running.set(false);
        if (vendorThread != null) {
            vendorThread.interrupt();
        }
        if (customerThread != null) {
            customerThread.interrupt();
        }
        System.out.println(message);
        broadcast(message);
    }


}