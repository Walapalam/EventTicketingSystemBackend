package com.OOP.EventTicketingSystemBackend.Controllers;

import com.OOP.EventTicketingSystemBackend.DTO.*;
import com.OOP.EventTicketingSystemBackend.Services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = adminService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/tickets")
    public ResponseEntity<List<TicketDTO>> getAllTickets() {
        List<TicketDTO> tickets = adminService.getAllTickets();
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/events")
    public ResponseEntity<List<EventDTO>> getAllEvents() {
        List<EventDTO> events = adminService.getAllEvents();
        return ResponseEntity.ok(events);
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionDTO>> getAllTransactions() {
        List<TransactionDTO> transactions = adminService.getAllTransactions();
        return ResponseEntity.ok(transactions);
    }

    @PostMapping("/simulate")
    public ResponseEntity<String> startSimulation() {
        adminService.startSimulation();
        return ResponseEntity.ok("Simulation started successfully");
    }

    @PostMapping("/stop-simulation")
    public ResponseEntity<String> stopSimulation() {
        adminService.stopSimulation("User request to stop simulation");
        return ResponseEntity.ok("Simulation stopped successfully");
    }
}