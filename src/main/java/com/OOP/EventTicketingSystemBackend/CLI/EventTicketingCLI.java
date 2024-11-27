package com.OOP.EventTicketingSystemBackend.CLI;

import com.OOP.EventTicketingSystemBackend.CLI.models.*;
import com.OOP.EventTicketingSystemBackend.CLI.services.TicketPool;
import com.OOP.EventTicketingSystemBackend.CLI.services.TransactionLog;
import com.OOP.EventTicketingSystemBackend.CLI.tasks.Customer;
import com.OOP.EventTicketingSystemBackend.CLI.tasks.Vendor;

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
            System.out.println("1. Login as Customer");
            System.out.println("2. Login as Vendor");
            System.out.println("3. View Transaction History");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    handleCustomerActions();
                    break;
                case 2:
                    handleVendorActions();
                    break;
                case 3:
                    viewTransactionHistory();
                    break;
                case 4:
                    running = false;
                    System.out.println("Exiting the system. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void handleCustomerActions() {
        System.out.print("Enter Customer ID: ");
        long customerId = scanner.nextLong();
        System.out.print("Enter Customer Name: ");
        String customerName = scanner.next();
        System.out.print("Enter Password: ");
        String password = scanner.next();

        Customer customer = new Customer(customerId, customerName, password);
        boolean customerMenu = true;

        while (customerMenu) {
            System.out.println("\nCustomer Menu:");
            System.out.println("1. Purchase a Ticket");
            System.out.println("2. Purchase Multiple Tickets");
            System.out.println("3. View Current Tickets");
            System.out.println("4. Logout");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    customer.purchaseTicket();
                    break;
                case 2:
                    System.out.print("Enter the number of tickets to purchase: ");
                    int ticketCount = scanner.nextInt();
                    Event dummyEvent = new Event("EVENT123", "Sample Event", ticketCount, 100.0); // Dummy event
                    customer.purchaseTickets(ticketCount, dummyEvent);
                    break;
                case 3:
                    System.out.println("Customer's Tickets: " + customer.toString());
                    break;
                case 4:
                    customerMenu = false;
                    System.out.println("Logged out as Customer.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void handleVendorActions() {
        System.out.print("Enter Vendor ID: ");
        long vendorId = scanner.nextLong();
        System.out.print("Enter Vendor Name: ");
        String vendorName = scanner.next();
        System.out.print("Enter Password: ");
        String password = scanner.next();

        Vendor vendor = new Vendor(vendorId, vendorName, password, ticketPool);
        boolean vendorMenu = true;

        while (vendorMenu) {
            System.out.println("\nVendor Menu:");
            System.out.println("1. Add an Event");
            System.out.println("2. Release Tickets for an Event");
            System.out.println("3. View Current Events");
            System.out.println("4. Logout");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter Event ID: ");
                    String eventId = scanner.next();
                    System.out.print("Enter Event Name: ");
                    String eventName = scanner.next();
                    System.out.print("Enter Number of Tickets: ");
                    int availableTickets = scanner.nextInt();
                    System.out.print("Enter Ticket Price: ");
                    double ticketPrice = scanner.nextDouble();
                    vendor.addEvent(eventId, eventName, availableTickets, ticketPrice);
                    break;
                case 2:
                    System.out.print("Enter Event ID to release tickets: ");
                    String releaseEventId = scanner.next();
                    Event event = vendor.getEvent(releaseEventId);
                    if (event != null) {
                        vendor.releaseTickets(event);
                    } else {
                        System.out.println("Event not found.");
                    }
                    break;
                case 3:
                    System.out.println("Vendor's Events: " + vendor.toString());
                    break;
                case 4:
                    vendorMenu = false;
                    System.out.println("Logged out as Vendor.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void viewTransactionHistory() {
        System.out.println("\nTransaction History:");
        for (Transaction transaction : transactionLog.getTransactionHistory()) {
            System.out.println(transaction);
        }
    }
}
