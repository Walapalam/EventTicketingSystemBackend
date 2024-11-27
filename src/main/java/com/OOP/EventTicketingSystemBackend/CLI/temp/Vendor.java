package com.OOP.EventTicketingSystemBackend.CLI.temp;

import java.util.Scanner;

public class Vendor extends User implements Runnable {
    Scanner scanner = new Scanner(System.in);
    public Vendor(String userName, String password) {
        super(userName, password);
    }

    public void createTickets(int noOfTickets, String eventName, boolean isAvailable, double ticketPrice) {
        for (int i = 0; i < noOfTickets; i++) {
            Ticket ticket = new Ticket("T" + i, eventName, true, ticketPrice);
            EventTicketingSystemCLI.ticketList.add(ticket);
        }
    }

    public void deleteTickets(String ticketNo) {
        EventTicketingSystemCLI.ticketList.removeIf(ticket -> ticket.getTicketNo().equals(ticketNo));
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public void cliMenu() {
        System.out.println("Enter username: ");
        setUserName(scanner.next());
        System.out.println("Enter password: ");
        setPassword(scanner.next());

        login();

        while (true) {
            System.out.println("Vendor Menu");
            System.out.println("1. Create Tickets");
            System.out.println("2. Delete Tickets");
            System.out.println("3. Logout");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("Enter number of tickets: ");
                    int noOfTickets = scanner.nextInt();
                    System.out.println("Enter event name: ");
                    String eventName = scanner.next();
                    System.out.println("Enter ticket price: ");
                    double ticketPrice = scanner.nextDouble();
                    createTickets(noOfTickets, eventName, true, ticketPrice);
                    break;
                case 2:
                    System.out.println("Enter ticket number to delete: ");
                    String ticketNo = scanner.next();
                    deleteTickets(ticketNo);
                    break;
                case 3:
                    logout();
                    return;
                default:
                    System.out.println("Invalid choice");
                    break;
            }
        }

    }

    @Override
    public void login() {
        if (EventTicketingSystemCLI.users.containsKey(userName) && EventTicketingSystemCLI.users.get(password).equals(password)) {
            System.out.println("Vendor " + super.userName + " logged in");
        } else {
            System.out.println("Invalid username or password");
        }
    }

    @Override
    public void logout() {
        System.out.println("Vendor " + super.userName + " logged out");
        System.exit(0);
    }

    @Override
    public void run() {
        System.out.println("CLI or GUI?");
        String choice = scanner.next();

        if (choice.equalsIgnoreCase("cli")) {
            cliMenu();
        } else if (choice.equalsIgnoreCase("gui")) {
            System.out.printf("GUI not implemented yet");
        } else {
            System.out.println("Invalid choice");
        }
    }
}
