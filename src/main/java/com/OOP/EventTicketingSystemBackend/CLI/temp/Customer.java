package com.OOP.EventTicketingSystemBackend.CLI.temp;

import java.util.Scanner;

public class Customer extends User implements Runnable{
    private Scanner scanner = new Scanner(System.in);

    public Customer(String userName, String password) {
        super(userName, password);
    }

    public void buyTicket() {
        if (EventTicketingSystemCLI.ticketList.isEmpty()) {
            System.out.println("No tickets available");
            return;
        } else {
            Ticket ticket = EventTicketingSystemCLI.ticketList.poll();
            System.out.println("Customer " + super.userName + " bought ticket " + ticket.getTicketNo());
        }
    }

    public void viewTickets() {
        if (EventTicketingSystemCLI.ticketList.isEmpty()) {
            System.out.println("No tickets available");
            return;
        } else {
            EventTicketingSystemCLI.ticketList.forEach(ticket -> System.out.println(ticket));
        }
    }

    public void cliMenu(){
        System.out.println("Enter username: ");
        setUserName(scanner.next());
        System.out.println("Enter password: ");
        setPassword(scanner.next());

        login();

        while(true) {
            System.out.println("Customer Menu");
            System.out.println("1. Buy Ticket");
            System.out.println("2. View Tickets");
            System.out.println("3. Logout");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    buyTicket();
                    break;
                case 2:
                    viewTickets();
                    break;
                case 3:
                    logout();
                    break;
                default:
                    System.out.println("Invalid choice");
                    break;
            }
        }
    }

    public void setUserName(String userName){
        this.userName = userName;
    }

    public void setPassword(String password){
        this.password = password;
    }

    @Override
    public void login() {
        if (EventTicketingSystemCLI.users.containsKey(super.userName) && EventTicketingSystemCLI.users.get(super.userName).equals(super.password)) {
            System.out.println("Customer " + super.userName + " logged in");
            return;
        } else {
            System.out.println("Invalid username or password, exiting...");
            System.exit(0);
        }
    }

    @Override
    public void logout() {
        System.out.println("Customer " + super.userName + " logged out");
    }

    @Override
    public void run() {
        System.out.println("CLI or GUI?");
        String choice = scanner.next();

        if (choice.equalsIgnoreCase("CLI")){
            cliMenu();
        } else {
            System.out.println("GUI not implemented yet");
        }
    }

}
