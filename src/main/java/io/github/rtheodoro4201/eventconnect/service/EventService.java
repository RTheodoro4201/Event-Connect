package io.github.rtheodoro4201.eventconnect.service;

import io.github.rtheodoro4201.eventconnect.dto.event.EventCreationDTO;
import io.github.rtheodoro4201.eventconnect.dto.event.EventUpdateDTO;
import io.github.rtheodoro4201.eventconnect.exception.EventAlreadyExistsException;
import io.github.rtheodoro4201.eventconnect.model.Event;
import io.github.rtheodoro4201.eventconnect.model.EventRegistration;
import io.github.rtheodoro4201.eventconnect.model.User;
import io.github.rtheodoro4201.eventconnect.repository.EventRegistrationRepository;
import io.github.rtheodoro4201.eventconnect.repository.EventRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@Transactional
public class EventService {
    private static final String ORGANIZER_NOT_FOUND_MESSAGE = "Organizer not found.";
    private static final String USER_NOT_FOUND_MESSAGE = "User not found.";
    private static final String USER_NOT_AUTHORIZED_MESSAGE = "You are not authorized to update this event.";
    private static final String USER_ALREADY_REGISTERED_MESSAGE = "User already registered at this event.";
    private static final String USER_NOT_REGISTERED_MESSAGE = "User is not registered at this event.";
    private static final String EVENT_ALREADY_CREATED_MESSAGE = "Event already created.";
    private static final String EVENT_NOT_FOUND_MESSAGE = "Event not found.";

    private final EventRepository eventRepository;
    private final EventRegistrationRepository eventRegistrationRepository;
    private final UserService userService;


    public EventService(EventRepository eventRepository, EventRegistrationRepository eventRegistrationRepository, UserService userService) {
        this.eventRepository = eventRepository;
        this.eventRegistrationRepository = eventRegistrationRepository;
        this.userService = userService;
    }

    @Transactional
    public Event createEvent(EventCreationDTO creationDTO, String organizerEmail) {
        User organizer = userService.findUserByEmail(organizerEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ORGANIZER_NOT_FOUND_MESSAGE));


        if (eventRepository.existsByTitleAndOrganizerId(creationDTO.getTitle(), organizer.getId())){
            throw new EventAlreadyExistsException(EVENT_ALREADY_CREATED_MESSAGE);
        }

        Event newEvent = new Event();
        newEvent.setTitle(creationDTO.getTitle());
        newEvent.setDescription(creationDTO.getDescription());
        newEvent.setDate(creationDTO.getDate());
        newEvent.setTime(creationDTO.getTime());
        newEvent.setLocation(creationDTO.getLocation());
        newEvent.setOrganizerId(organizer.getId());
        newEvent.setMaxParticipants(creationDTO.getMaxParticipants());

        return eventRepository.save(newEvent);
    }

    public Optional<Event> updateEvent(Long eventId, EventUpdateDTO updateDTO, String organizerEmail) {
        return eventRepository.findById(eventId)
                .flatMap(updatedEvent -> {
                    User organizer = userService.findUserByEmail(organizerEmail)
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, USER_NOT_FOUND_MESSAGE));

                    if (!updatedEvent.getOrganizerId().equals(organizer.getId())) {
                        throw new ResponseStatusException(HttpStatus.FORBIDDEN, USER_NOT_AUTHORIZED_MESSAGE);
                    }

                    updateDTO.getTitle().ifPresent(updatedEvent::setTitle);
                    updateDTO.getDescription().ifPresent(updatedEvent::setDescription);
                    updateDTO.getDate().ifPresent(updatedEvent::setDate);
                    updateDTO.getTime().ifPresent(updatedEvent::setTime);
                    updateDTO.getLocation().ifPresent(updatedEvent::setLocation);
                    updateDTO.getMaxParticipants().ifPresent(updatedEvent::setMaxParticipants);

                    return Optional.of(eventRepository.save(updatedEvent));
                });
    }

    public Optional<Event> getEventById(Long eventId) {
        return eventRepository.findById(eventId);
    }
    public void registerUserForEvent(Long eventId, String userEmail) {
            User user = userService.findUserByEmail(userEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, USER_NOT_FOUND_MESSAGE));

            Event event = eventRepository.findById(eventId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, EVENT_NOT_FOUND_MESSAGE));

            if (eventRegistrationRepository.existsById_UserIdAndId_EventId(user.getId(), eventId)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, USER_ALREADY_REGISTERED_MESSAGE);
            }

            EventRegistration registration = new EventRegistration(user, event);
            eventRegistrationRepository.save(registration);
    }

    public void unregisterUserFromEvent(Long eventId, String userEmail) {
        User user = userService.findUserByEmail(userEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, USER_NOT_FOUND_MESSAGE));

        Event event = eventRepository.findById(eventId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, EVENT_NOT_FOUND_MESSAGE));

        if (!eventRegistrationRepository.existsById_UserIdAndId_EventId(user.getId(), event.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, USER_NOT_REGISTERED_MESSAGE);
        }

        eventRegistrationRepository.deleteById_UserIdAndId_EventId(user.getId(), event.getId());
    }

    public void deleteEvent(Long eventId){
        Event event = eventRepository.findById(eventId).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, EVENT_NOT_FOUND_MESSAGE));

        eventRegistrationRepository.deleteAllById_EventId(event.getId());
        eventRepository.delete(event);
    }
}