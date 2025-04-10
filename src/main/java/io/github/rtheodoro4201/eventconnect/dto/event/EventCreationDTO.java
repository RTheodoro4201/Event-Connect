package io.github.rtheodoro4201.eventconnect.dto.event;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class EventCreationDTO implements Serializable {
    @NotNull
    @Size(max = 255)
    String title;

    @Size(max = 255)
    String description;

    @NotNull
    LocalDate date;

    @NotNull
    LocalTime time;

    @Size(max = 255)
    String location;

    @NotNull
    @PositiveOrZero
    Long organizerId;

    @PositiveOrZero
    Long maxParticipants;
}