package io.github.rtheodoro4201.eventconnect.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "event_registrations")
public class EventRegistration {
    @EmbeddedId
    private EventRegistrationId id;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @MapsId("eventId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @CreationTimestamp
    @Column(name = "registered_at", nullable = false)
    private Instant registeredAt;

    public EventRegistration() {
        this.id = new EventRegistrationId();
    }

    public EventRegistration(User user, Event event) {
        this.id = new EventRegistrationId();
        this.user = user;
        this.event = event;
    }

}