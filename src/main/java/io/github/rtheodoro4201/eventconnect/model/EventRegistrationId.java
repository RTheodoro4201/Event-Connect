package io.github.rtheodoro4201.eventconnect.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class EventRegistrationId implements Serializable {
    @Serial
    private static final long serialVersionUID = 4393123544873081572L;

    @NotNull
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @NotNull
    @Column(name = "event_id", nullable = false)
    private Long eventId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        EventRegistrationId entity = (EventRegistrationId) o;
        return Objects.equals(this.eventId, entity.eventId) &&
                Objects.equals(this.userId, entity.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId, userId);
    }

    public EventRegistrationId() {
    }

    public EventRegistrationId(Long userId, Long eventId) {
        this.userId = userId;
        this.eventId = eventId;
    }
}