package com.OOP.EventTicketingSystemBackend.Services;

import com.OOP.EventTicketingSystemBackend.CLI.models.Event;
import com.OOP.EventTicketingSystemBackend.CLI.repositories.EventRepository;
import com.OOP.EventTicketingSystemBackend.DTO.EventDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    // Create a method that checks the no of tickets available in the event and reduce it if someone purchases a ticket
    public int checkEventTicketCount (EventDTO eventDTO){
        Event event = convertToEntity(eventDTO);
        return event.getAvailableTickets();
    }

    // Reduce the no of tickets available in the event
    public int decrementEventTicketCount (EventDTO eventDTO, int ticketCount){
        Event event = convertToEntity(eventDTO);
        event.decrementTickets(ticketCount);
        return event.getAvailableTickets();
    }

    // Creating an Event from a DTO
    public EventDTO createEvent(EventDTO eventDTO){
        Event event = convertToEntity(eventDTO);
        eventRepository.save(event);
        return convertToDTO(event);
    }

    // Getting Event by EventId from DTO
    public EventDTO getEventById(long eventId){
        Optional<Event> event = eventRepository.findById((int) eventId);
        return event.map(this::convertToDTO).orElse(null);
    }

    // Update a given DTO
    public EventDTO updateEvent(EventDTO eventDTO){
        if (eventRepository.existsById((int) eventDTO.getEventID())){
            Event event = convertToEntity(eventDTO);
            eventRepository.save(event);
            return convertToDTO(event);
        }
        return null; // Have an error code, or throw an exception to show that the event does not exist
    }

    // Delete a given DTO
    public void deleteEvent(long eventId){
        eventRepository.deleteById((int) eventId);
    }

    // Get all events
    public List<EventDTO> getAllEvents(){
        return eventRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    // DTO to Entity
    private Event convertToEntity (EventDTO eventDTO){
        Event event = new Event(eventDTO.getEventName(), eventDTO.getAvailableTickets(), eventDTO.getTicketPrice());
        return event;
    }

    // Event to DTO
    private EventDTO convertToDTO (Event event){
        EventDTO eventDTO = new EventDTO(event.getEventID(), event.getEventName(), event.getAvailableTickets(), event.getTicketPrice());
        return eventDTO;
    }

}