package com.OOP.EventTicketingSystemBackend.CLI;

import com.OOP.EventTicketingSystemBackend.CLI.models.*;
import com.OOP.EventTicketingSystemBackend.CLI.repositories.ConfigRepository;
import com.OOP.EventTicketingSystemBackend.CLI.repositories.UserRepository;
import com.OOP.EventTicketingSystemBackend.CLI.services.TicketPool;
import com.OOP.EventTicketingSystemBackend.CLI.services.TransactionLog;
import com.OOP.EventTicketingSystemBackend.CLI.tasks.Customer;
import com.OOP.EventTicketingSystemBackend.CLI.tasks.Vendor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class EventTicketingCLI {
    private static Scanner scanner = new Scanner(System.in);
    private static TicketPool ticketPool = TicketPool.getInstance();
    private static TransactionLog transactionLog = TransactionLog.getInstance();

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConfigRepository configRepository;


    public void run() {
        System.out.println("Welcome to the Event Ticketing System CLI!");
        System.out.println("============================================");

        Configuration.getConfigurationFromDatabase(1,"src/main/java/com/OOP/EventTicketingSystemBackend/CLI/config.json", configRepository);
        boolean running = true;

        while (running) {
            System.out.println("\nMain Menu:");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. View Transaction History");
            System.out.println("4. Registered users");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    handleRegistration();
                    break;
                case 2:
                    handleUserLogin();
                    break;
                case 3:
                    viewTransactionHistory();
                    break;
                case 4:
                    seeUsers();
                    break;
                case 5:
                    running = false;
                    Configuration.saveConfigurationToDatabase(configRepository);
                    System.out.println("Exiting the system. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void handleRegistration() {
        System.out.println("Enter username: ");
        String username = scanner.next();
        System.out.println("Enter password: ");
        String password = scanner.next();
        System.out.println("Enter role (vendor/customer): ");
        String role = scanner.next();

        if (userRepository.existsByUsername(username)) {
            System.out.println("Username already taken");
            return;
        }

        User user = null;
        if (role.equalsIgnoreCase("vendor")) {
            user = new Vendor(username, password, ticketPool, "vendor");
            System.out.println(user.getRole());
            System.out.println("Added vendor");
        } else if (role.equalsIgnoreCase("customer")) {
            user = new Customer(username, password, "customer");
            System.out.println("Added customer");
            System.out.println(user.getRole());
        }

        if (user != null) {
            try {
                userRepository.save(user);
                System.out.println("User added to repository");
            } catch (RuntimeException e) {
                System.out.println(e);
            }
        } else {
            System.out.println("User role not specified");
        }
    }

    private void seeUsers() {
        System.out.println("Registered Users:");

        for (User user : userRepository.findAll()) {
            System.out.println(user.toString());
        }
    }

    private void handleUserLogin() {
        System.out.println("Enter username: ");
        String username = scanner.next();
        System.out.println("Enter password: ");
        String password = scanner.next();

        User user = userRepository.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            System.out.printf("User found: %s - %s\n", user.getUsername(), user.getRole());
            if (user.getRole().equalsIgnoreCase("vendor")) {
                System.out.printf("Logged in as %s - %s\n", username, user.getRole());
                handleVendorActions((Vendor) user);
            } else if (user.getRole().equalsIgnoreCase("customer")) {
                System.out.printf("Logged in as %s - %s\n", username, user.getRole());
                handleCustomerActions((Customer) user);
            } else {
                System.out.println("Invalid role.");
            }
        } else {
            System.out.println("Invalid username or password.");
        }
    }

    private void handleCustomerActions(Customer customer) {
        Thread custThread = new Thread(customer);
        custThread.start();

        try {
            custThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleVendorActions(Vendor vendor) {
        Thread vendorThread = new Thread(vendor);
        vendorThread.start();
        try {
            vendorThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void viewTransactionHistory() {
        System.out.println("\nTransaction History:");
        for (Transaction transaction : transactionLog.getTransactionHistory()) {
            System.out.println(transaction);
        }
    }
}