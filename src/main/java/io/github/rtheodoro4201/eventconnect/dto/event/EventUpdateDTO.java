package io.github.rtheodoro4201.eventconnect.dto.event;

import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

/**
 * DTO for {@link io.github.rtheodoro4201.eventconnect.model.Event}
 */
@Value
public class EventUpdateDTO implements Serializable {
    Optional<@Size(max = 255) String> title;
    Optional<@Size(max = 255) String> description;
    Optional<LocalDate> date;
    Optional<LocalTime> time;
    Optional<@Size(max = 255) String> location;
    Optional<@PositiveOrZero Long> maxParticipants;
}
