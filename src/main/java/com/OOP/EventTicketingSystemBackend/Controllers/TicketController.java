package com.OOP.EventTicketingSystemBackend.Controllers;

import com.OOP.EventTicketingSystemBackend.DTO.TicketDTO;
import com.OOP.EventTicketingSystemBackend.Services.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @PostMapping("/purchase")
    public ResponseEntity<TicketDTO> purchaseTicket(@RequestBody TicketDTO ticketDTO) {
        TicketDTO purchasedTicket = ticketService.purchaseTicket(ticketDTO);
        return ResponseEntity.ok(purchasedTicket);
    }

    @PostMapping("/release")
    public ResponseEntity<Void> releaseTicket(@RequestBody TicketDTO ticketDTO) {
        ticketService.releaseTicket(ticketDTO);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketDTO> getTicketById(@PathVariable long id) {
        TicketDTO ticketDTO = ticketService.getTicketById(id);
        return ResponseEntity.ok(ticketDTO);
    }
}