package io.github.rtheodoro4201.eventconnect.util;

import io.github.rtheodoro4201.eventconnect.model.Event;
import io.github.rtheodoro4201.eventconnect.model.User;
import io.github.rtheodoro4201.eventconnect.repository.EventRepository;
import io.github.rtheodoro4201.eventconnect.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Component
public class EntityUtils {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    public EntityUtils(UserRepository userRepository, EventRepository eventRepository) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    public <T> T checkEntity(Long entityId, Class<T> entityClass) {
        if (entityClass.equals(User.class)) {
            Optional<User> userOptional = userRepository.findById(entityId);
            return userOptional.map(entityClass::cast)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found."));
        } else if (entityClass.equals(Event.class)) {
            Optional<Event> eventOptional = eventRepository.findById(entityId);
            return eventOptional.map(entityClass::cast)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found."));
        } else {
            throw new IllegalArgumentException("Unsupported entity type: " + entityClass.getName());
        }
    }
}
