package io.github.rtheodoro4201.eventconnect.controller;

import io.github.rtheodoro4201.eventconnect.dto.event.EventCreationDTO;
import io.github.rtheodoro4201.eventconnect.dto.event.EventUpdateDTO;
import io.github.rtheodoro4201.eventconnect.model.Event;
import io.github.rtheodoro4201.eventconnect.service.AuthenticatedUserService;
import io.github.rtheodoro4201.eventconnect.service.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;
    private final AuthenticatedUserService authenticatedUserService;

    public EventController(EventService eventService, AuthenticatedUserService authenticatedUserService) {
        this.eventService = eventService;
        this.authenticatedUserService = authenticatedUserService;
    }

    @PostMapping("")
    public ResponseEntity<Event> create(@RequestBody EventCreationDTO creationDTO) {
        String organizerEmail = authenticatedUserService.getCurrentUserEmail();
        Event registeredEvent = eventService.createEvent(creationDTO, organizerEmail);
        return new ResponseEntity<>(registeredEvent, HttpStatus.CREATED);
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<Event> update(@RequestBody EventUpdateDTO updateDTO, @PathVariable Long eventId) {
        String organizerEmail = authenticatedUserService.getCurrentUserEmail();
        Optional<Event> updatedEvent = eventService.updateEvent(eventId, updateDTO , organizerEmail);
        return updatedEvent.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<Event> getEvent(@PathVariable Long eventId){
        Optional<Event> eventOptional = eventService.getEventById(eventId);
        return eventOptional.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long eventId) {
        eventService.deleteEvent(eventId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{eventId}/registrations")
    public ResponseEntity<Void> registerForEvent(@PathVariable Long eventId) {
        try {
            String userEmail = authenticatedUserService.getCurrentUserEmail();
            eventService.registerUserForEvent(eventId, userEmail);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        }
    }

    @DeleteMapping("/{eventId}/registrations")
    public ResponseEntity<Void> unregisterFromEvent(@PathVariable Long eventId) {
        try {
            String userEmail = authenticatedUserService.getCurrentUserEmail();
            eventService.unregisterUserFromEvent(eventId, userEmail);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        }
    }
}
