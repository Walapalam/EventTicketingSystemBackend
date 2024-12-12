package com.OOP.EventTicketingSystemBackend.Services;

import com.OOP.EventTicketingSystemBackend.CLI.models.Event;
import com.OOP.EventTicketingSystemBackend.CLI.models.Ticket;
import com.OOP.EventTicketingSystemBackend.CLI.repositories.EventRepository;
import com.OOP.EventTicketingSystemBackend.CLI.repositories.TicketRepository;
import com.OOP.EventTicketingSystemBackend.CLI.repositories.TransactionRepository;
import com.OOP.EventTicketingSystemBackend.CLI.tasks.Customer;
import com.OOP.EventTicketingSystemBackend.CLI.tasks.Vendor;
import com.OOP.EventTicketingSystemBackend.DTO.TicketDTO;
import com.OOP.EventTicketingSystemBackend.DTO.EventDTO;
import com.OOP.EventTicketingSystemBackend.DTO.TransactionDTO;
import com.OOP.EventTicketingSystemBackend.DTO.UserDTO;
import com.OOP.EventTicketingSystemBackend.DTO.TicketPurchaseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserService userService;

    public EventDTO releaseNewEvent(UserDTO userDTO, String eventName, int numberOfTickets, double ticketPrice) {
        Vendor vendor = (Vendor) userService.getUserByRole(userDTO);

        Event event = new Event(eventName, numberOfTickets, ticketPrice);
        event.setReleasedBy(vendor);
        eventRepository.save(event);

        for (int i = 0; i < numberOfTickets; i++) {
            Ticket ticket = new Ticket(eventName, ticketPrice, event);
            vendor.releaseTicket(ticket);
            ticketRepository.save(ticket);
        }

        return new EventDTO(event);
    }

    public TicketDTO releaseNewTicket(UserDTO userDTO, String eventName, double ticketPrice) {
        Vendor vendor = (Vendor) userService.getUserByRole(userDTO);

        Event event = eventRepository.findByEventName(eventName);
        if (event == null) {
            event = new Event(eventName, 1, ticketPrice);
            event.setReleasedBy(vendor);
            eventRepository.save(event);
        }

        Ticket ticket = new Ticket(eventName, ticketPrice, event);
        vendor.releaseTicket(ticket);
        ticketRepository.save(ticket);

        return new TicketDTO(ticket);
    }

    public TicketDTO purchaseTicket(TicketPurchaseDTO ticketPurchaseDTO) {
        UserDTO userDTO = ticketPurchaseDTO.getUserDTO();
        Long eventID = ticketPurchaseDTO.getEventID();

        Customer customer = (Customer) userService.getUserByRole(userDTO);
        Event event = eventRepository.findById(eventID.intValue()).orElseThrow(() -> new RuntimeException("Event not found"));

        if (event.getAvailableTickets() > 0) {
            Ticket ticket = new Ticket(event.getEventName(), event.getTicketPrice(), event);
            customer.purchaseTicket(ticket);
            ticketRepository.save(ticket);

            event.setAvailableTickets(event.getAvailableTickets() - 1);
            eventRepository.save(event);

            return new TicketDTO(ticket);
        } else {
            throw new RuntimeException("No tickets available");
        }
    }

    public List<TicketDTO> viewAllTicketsReleasedByVendor(UserDTO userDTO) {
        Vendor vendor = (Vendor) userService.getUserByRole(userDTO);

        return ticketRepository.findAll().stream()
                .filter(ticket -> ticket.getEvent().getReleasedBy().equals(vendor))
                .map(TicketDTO::new)
                .collect(Collectors.toList());
    }

    public List<EventDTO> viewAllEventsManagedByVendor(UserDTO userDTO) {
        Vendor vendor = (Vendor) userService.getUserByRole(userDTO);

        return eventRepository.findAll().stream()
                .filter(event -> event.getReleasedBy().equals(vendor))
                .map(EventDTO::new)
                .collect(Collectors.toList());
    }

    public List<TransactionDTO> viewTransactionsRelatedToVendor(UserDTO userDTO) {
        Vendor vendor = (Vendor) userService.getUserByRole(userDTO);

        return transactionRepository.findAll().stream()
                .filter(transaction -> transaction.getUser().equals(vendor))
                .map(TransactionDTO::new)
                .collect(Collectors.toList());
    }
}