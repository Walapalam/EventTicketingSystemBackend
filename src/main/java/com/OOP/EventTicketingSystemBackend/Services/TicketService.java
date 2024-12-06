package com.OOP.EventTicketingSystemBackend.Services;

import com.OOP.EventTicketingSystemBackend.DTO.TicketDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TicketService {

    private List<TicketDTO> ticketStore = new ArrayList<>();
    private long currentId = 1;

    public TicketDTO purchaseTicket(TicketDTO ticketDTO) {
        ticketDTO.setTicketId(currentId++);
        ticketStore.add(ticketDTO);
        return ticketDTO;
    }

    public void releaseTicket(TicketDTO ticketDTO) {
        ticketStore.removeIf(ticket -> ticket.getTicketId() == ticketDTO.getTicketId());
    }

    public TicketDTO getTicketById(long id) {
        return ticketStore.stream()
                .filter(ticket -> ticket.getTicketId() == id)
                .findFirst()
                .orElse(null);
    }
}