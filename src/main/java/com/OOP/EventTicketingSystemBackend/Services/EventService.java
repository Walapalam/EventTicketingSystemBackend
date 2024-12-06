package com.OOP.EventTicketingSystemBackend.Services;

import com.OOP.EventTicketingSystemBackend.DTO.EventDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    private List<EventDTO> eventStore = new ArrayList<>();
    private long currentId = 1;

    public EventDTO createEvent(EventDTO eventDTO) {
        eventDTO.setEventID(currentId++);
        eventStore.add(eventDTO);
        return eventDTO;
    }

    public EventDTO getEventById(long id) {
        return eventStore.stream()
                .filter(event -> event.getEventID() == id)
                .findFirst()
                .orElse(null);
    }

    public EventDTO updateEvent(long id, EventDTO eventDTO) {
        Optional<EventDTO> existingEvent = eventStore.stream()
                .filter(event -> event.getEventID() == id)
                .findFirst();

        if (existingEvent.isPresent()) {
            eventDTO.setEventID(id);
            eventStore.remove(existingEvent.get());
            eventStore.add(eventDTO);
            return eventDTO;
        }
        return null;
    }

    public void deleteEvent(long id) {
        eventStore.removeIf(event -> event.getEventID() == id);
    }

    public List<EventDTO> getAllEvents() {
        return new ArrayList<>(eventStore);
    }
}