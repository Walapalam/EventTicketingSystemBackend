package com.OOP.EventTicketingSystemBackend.CLI.models;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Scanner;

import com.OOP.EventTicketingSystemBackend.CLI.repositories.ConfigRepository;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Entity
@Component
@ConfigurationProperties(prefix = "configuration")
public class Configuration {
    // Configurable parameters

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long ID;

    //Find a way to set these 4 into the values of the other 4 static variables
    @Column
    public int TotalTickets = 1000;
    @Column
    public int TicketReleaseRate = 5;
    @Column
    public int CustomerRetrievalRate = 3;
    @Column
    public int MaxTicketCapacity = 500;


    public static int totalTickets = 1000;
    public static int ticketReleaseRate = 5;
    public static int customerRetrievalRate = 3;
    public static int maxTicketCapacity = 500;

    // For saving into DB
    public Configuration() {
        this.TotalTickets = totalTickets;
        this.TicketReleaseRate = ticketReleaseRate;
        this.CustomerRetrievalRate = customerRetrievalRate;
        this.MaxTicketCapacity = maxTicketCapacity;
    }

    // Method to display current configuration
    public static void displayConfiguration() {
        System.out.println("Current Configuration:");
        System.out.println("Total Tickets: " + totalTickets);
        System.out.println("Ticket Release Rate: " + ticketReleaseRate + " tickets/sec");
        System.out.println("Customer Retrieval Rate: " + customerRetrievalRate + " tickets/sec");
        System.out.println("Max Ticket Capacity: " + maxTicketCapacity);
    }

    // Method to update configuration manually
    public static void updateConfiguration(int totalTickets, int ticketReleaseRate, int customerRetrievalRate, int maxTicketCapacity) {
        Configuration.totalTickets = totalTickets;
        Configuration.ticketReleaseRate = ticketReleaseRate;
        Configuration.customerRetrievalRate = customerRetrievalRate;
        Configuration.maxTicketCapacity = maxTicketCapacity;
    }

    // Method to set configurations via CLI input
    public static void configureViaCLI() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter Total Tickets: ");
        totalTickets = scanner.nextInt();

        System.out.print("Enter Ticket Release Rate (tickets/sec): ");
        ticketReleaseRate = scanner.nextInt();

        System.out.print("Enter Customer Retrieval Rate (tickets/sec): ");
        customerRetrievalRate = scanner.nextInt();

        System.out.print("Enter Max Ticket Capacity: ");
        maxTicketCapacity = scanner.nextInt();

        System.out.println("Configuration updated successfully via CLI!");
        displayConfiguration();
    }

    // Method to set configurations via GUI input (placeholder)
    public static void configureViaGUI(int totalTickets, int ticketReleaseRate, int customerRetrievalRate, int maxTicketCapacity) {
        updateConfiguration(totalTickets, ticketReleaseRate, customerRetrievalRate, maxTicketCapacity);
        System.out.println("Configuration updated successfully via GUI!");
        displayConfiguration();
    }

    // Method to load configurations from a JSON file
    // Throws error, check for JSON file, if available config will load, if not will have to use CLI
    public static void configureFromJSON(String filePath){
        FileReader reader = null;
        try {
            reader = new FileReader(filePath);
        } catch (FileNotFoundException e) {
            System.out.println("JSON file not found. Configuring via CLI...");
            configureViaCLI();
            return;
        }

        JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();

        totalTickets = jsonObject.get("totalTickets").getAsInt();
        ticketReleaseRate = jsonObject.get("ticketReleaseRate").getAsInt();
        customerRetrievalRate = jsonObject.get("customerRetrievalRate").getAsInt();
        maxTicketCapacity = jsonObject.get("maxTicketCapacity").getAsInt();

        System.out.println("Configuration loaded successfully from JSON!");
        displayConfiguration();
    }

    // Method to save current configuration to a JSON file
    public static void saveConfigurationToJSON(String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("totalTickets", totalTickets);
            jsonObject.addProperty("ticketReleaseRate", ticketReleaseRate);
            jsonObject.addProperty("customerRetrievalRate", customerRetrievalRate);
            jsonObject.addProperty("maxTicketCapacity", maxTicketCapacity);

            writer.write(jsonObject.toString());
            System.out.println("Configuration saved successfully to JSON!");
        } catch (IOException e) {
            System.err.println("Error writing to JSON file: " + e.getMessage());
        }
    }

    public static void getConfigurationFromDatabase(long ID, String filepath, ConfigRepository configurationRepository) {
        Configuration configuration = configurationRepository.findById((int) ID).orElse(null);
        if (configuration == null) {
            System.out.println("Configuration not found.\n Configuring from JSON file...");
            Configuration.configureFromJSON(filepath);
        } else {
            System.out.println("Configuration found.");
            System.out.println("Total Tickets: " + configuration.TotalTickets);
            System.out.println("Ticket Release Rate: " + configuration.TicketReleaseRate + " tickets/sec");
            System.out.println("Customer Retrieval Rate: " + configuration.CustomerRetrievalRate + " tickets/sec");
            System.out.println("Max Ticket Capacity: " + configuration.MaxTicketCapacity);
        }
    }

    public static void saveConfigurationToDatabase(ConfigRepository configurationRepository) {
        Configuration configuration = new Configuration();
        configurationRepository.save(configuration);
        System.out.println("Configuration saved to database.");
    }
}

