package com.OOP.EventTicketingSystemBackend.CLI.repositories;

import com.OOP.EventTicketingSystemBackend.CLI.models.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {
    Ticket findByTicketId(long ticketId);
    boolean existsByTicketId(long ticketId);
}
