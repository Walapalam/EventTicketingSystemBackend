package com.OOP.EventTicketingSystemBackend.CLI;

import com.OOP.EventTicketingSystemBackend.CLI.models.*;
import com.OOP.EventTicketingSystemBackend.CLI.repositories.UserRepository;
import com.OOP.EventTicketingSystemBackend.CLI.services.TicketPool;
import com.OOP.EventTicketingSystemBackend.CLI.services.TransactionLog;
import com.OOP.EventTicketingSystemBackend.CLI.tasks.Customer;
import com.OOP.EventTicketingSystemBackend.CLI.tasks.Vendor;
import com.OOP.EventTicketingSystemBackend.CLI.models.User;

import java.util.Scanner;

public class EventTicketingCLI {
    private static Scanner scanner = new Scanner(System.in);
    private static TicketPool ticketPool = TicketPool.getInstance();
    private static TransactionLog transactionLog = TransactionLog.getInstance();

    public static void main(String[] args) {
        System.out.println("Welcome to the Event Ticketing System CLI!");
        System.out.println("============================================");
        boolean running = true;

        while (running) {
            System.out.println("\nMain Menu:");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. View Transaction History");
            System.out.println("4. Registered users"); //
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
                    UserRepository.seeUsers();
                    break;
                case 5:
                    running = false;
                    System.out.println("Exiting the system. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void handleRegistration() {
        System.out.println("Enter username: ");
        String username = scanner.next();
        System.out.println("Enter password: ");
        String password = scanner.next();
        System.out.println("Enter role (vendor/customer): ");
        String role = scanner.next();

        if (UserRepository.checkUsername(username)) {
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
                UserRepository.addUser(user);
                System.out.println("User added to repository");
            } catch (RuntimeException e) {
                System.out.println(e);
            }
        } else {
            System.out.println("User role not specified");
        }
    }

    private static void handleUserLogin() {
        System.out.println("Enter username: ");
        String username = scanner.next();
        System.out.println("Enter password: ");
        String password = scanner.next();

        User user = null;
        for (User u : UserRepository.getUsers()) {
            if (u.getUserName().equals(username) && u.getPassword().equals(password)) {
                user = u;
                break;
            }
        }

        if (user != null) {
            System.out.printf("User found: %s - %s\n", user.getUserName(), user.getRole()); // Debug print
            if (user.getRole().equalsIgnoreCase("vendor")) {
                System.out.printf("Logged in as %s - %s\n", username, user.getRole());
                handleVendorActions(user);
            } else if (user.getRole().equalsIgnoreCase("customer")) {
                System.out.printf("Logged in as %s - %s\n", username, user.getRole());
                handleCustomerActions(user);
            } else {
                System.out.println("Invalid role.");
            }
        } else {
            System.out.println("Invalid username or password.");
        }
    }

    private static void handleCustomerActions(User user) {
        Customer customer = new Customer(user.getUserName(), user.getPassword(), "customer");
        Thread custThread = new Thread(customer);
        custThread.start();

        try {
            custThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void handleVendorActions(User user) {
        System.out.println(Thread.currentThread());

        Vendor vendor = new Vendor(user.getUserName(), user.getPassword(), ticketPool, "vendor");
        Thread vendorThread = new Thread(vendor);
        vendorThread.start();
        try {
            vendorThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void viewTransactionHistory() {
        System.out.println("\nTransaction History:");
        for (Transaction transaction : transactionLog.getTransactionHistory()) {
            System.out.println(transaction);
        }
    }
}
