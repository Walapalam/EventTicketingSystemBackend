package com.OOP.EventTicketingSystemBackend.Controllers;

import com.OOP.EventTicketingSystemBackend.DTO.TicketDTO;
import com.OOP.EventTicketingSystemBackend.DTO.TransactionDTO;
import com.OOP.EventTicketingSystemBackend.DTO.UserDTO;
import com.OOP.EventTicketingSystemBackend.DTO.EventDTO;
import com.OOP.EventTicketingSystemBackend.DTO.TicketPurchaseDTO;
import com.OOP.EventTicketingSystemBackend.Services.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @PostMapping("/releaseEvent")
    public ResponseEntity<EventDTO> releaseNewEvent(@RequestBody UserDTO userDTO, @RequestParam String eventName, @RequestParam int numberOfTickets, @RequestParam double ticketPrice) {
        EventDTO eventDTO = ticketService.releaseNewEvent(userDTO, eventName, numberOfTickets, ticketPrice);
        return ResponseEntity.ok(eventDTO);
    }

    @PostMapping("/releaseTicket")
    public ResponseEntity<TicketDTO> releaseNewTicket(@RequestBody UserDTO userDTO, @RequestParam String eventName, @RequestParam double ticketPrice) {
        TicketDTO ticketDTO = ticketService.releaseNewTicket(userDTO, eventName, ticketPrice);
        return ResponseEntity.ok(ticketDTO);
    }

    @PostMapping("/purchase")
    public ResponseEntity<TicketDTO> purchaseTicket(@RequestBody TicketPurchaseDTO ticketPurchaseDTO) {
        TicketDTO ticketDTO = ticketService.purchaseTicket(ticketPurchaseDTO);
        return ResponseEntity.ok(ticketDTO);
    }

    @GetMapping("/viewTickets")
    public ResponseEntity<List<TicketDTO>> viewAllTicketsReleasedByVendor(@RequestBody UserDTO userDTO) {
        List<TicketDTO> tickets = ticketService.viewAllTicketsReleasedByVendor(userDTO);
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/viewEvents")
    public ResponseEntity<List<EventDTO>> viewAllEventsManagedByVendor(@RequestBody UserDTO userDTO) {
        List<EventDTO> events = ticketService.viewAllEventsManagedByVendor(userDTO);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/viewTransactions")
    public ResponseEntity<List<TransactionDTO>> viewTransactionsRelatedToVendor(@RequestBody UserDTO userDTO) {
        List<TransactionDTO> transactions = ticketService.viewTransactionsRelatedToVendor(userDTO);
        return ResponseEntity.ok(transactions);
    }
}