package com.OOP.EventTicketingSystemBackend.CLI.temp;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;

// For now the program has different logins for vendor and customer, down the line make it common
// ..and have the user type as an argument, that decides whether to create a vendor thread or a customer thread
public class EventTicketingSystemCLI {
    public static Map<String, String> users = new HashMap<>();
    public static ConcurrentLinkedQueue<Ticket> ticketList = new ConcurrentLinkedQueue<Ticket>();
    public static int ticketNo = 1;
    public static Scanner scanner = new Scanner(System.in);

    static {
        users.put("customer", "password");
        users.put("vendor", "password");
    }

    public static void main(String[] args) {
        LoadConfig config = new LoadConfig();
        config.systemConfig();

        while (true) {
            System.out.println("Enter username: ");
            String userName = scanner.next();
            System.out.println("Enter password: ");
            String password = scanner.next();

            if (users.containsKey(userName) && users.get(userName).equals(password)) {
                System.out.println("Login successful");
                break;
            } else {
                System.out.println("Invalid credentials");
            }
        }
        System.out.println("Customer or Vendor?");
        String choice = scanner.next();

        if (choice.equalsIgnoreCase("customer")) {
            Customer customer = new Customer("customer", "password");
            customer.run();
        } else if (choice.equalsIgnoreCase("vendor")) {
            Vendor vendor = new Vendor("vendor", "password");
            vendor.run();
        } else {
            System.out.println("Invalid choice");
        }

    }


}
