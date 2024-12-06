package com.OOP.EventTicketingSystemBackend.CLI.repositories;

import com.OOP.EventTicketingSystemBackend.CLI.models.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Integer> {
}
