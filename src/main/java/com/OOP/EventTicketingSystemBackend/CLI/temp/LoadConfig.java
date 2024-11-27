package com.OOP.EventTicketingSystemBackend.CLI.temp;

import java.util.Scanner;

public class LoadConfig {
    public static int totalTickets;
    public static int ticketReleaseRate;
    public static int customerRetrievalRate;
    public static int maxTicketCapacity;

    private final Scanner input = new Scanner(System.in);

    public void systemConfig(){
        System.out.println("System Configuration\n");

        totalTickets = getValidInput("Enter the total number of tickets: ", 1, Integer.MAX_VALUE);
        ticketReleaseRate = getValidInput("Enter the ticket release rate: ", 1, 60);
        customerRetrievalRate = getValidInput("Enter the customer retrieval rate: ", 1, 60);
        maxTicketCapacity = getValidInput("Enter the maximum ticket capacity: ", 1, Integer.MAX_VALUE);

    }

    public int getValidInput(String prompt, int min, int max){
        int input;

        while (true){
            System.out.print(prompt);
            input = this.input.nextInt();

            if (input >= min && input <= max){
                break;
            } else {
                System.out.println("Invalid input. Please enter a value between " + min + " and " + max + ".");
            }
        }

        return input;
    }

    public int getTotalTickets() {
        return totalTickets;
    }

    public int getTicketReleaseRate() {
        return ticketReleaseRate;
    }

    public int getCustomerRetrievalRate() {
        return customerRetrievalRate;
    }

    public int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }

    @Override
    public String toString() {
        return String.format("Total Tickets: %d\nTicket Release Rate: %d\nCustomer Retrieval Rate: %d\nMax Ticket Capacity: %d\n",
                totalTickets, ticketReleaseRate, customerRetrievalRate, maxTicketCapacity);
    }
}
