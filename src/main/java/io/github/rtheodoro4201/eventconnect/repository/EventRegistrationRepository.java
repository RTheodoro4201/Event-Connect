package io.github.rtheodoro4201.eventconnect.repository;

import io.github.rtheodoro4201.eventconnect.model.EventRegistration;
import io.github.rtheodoro4201.eventconnect.model.EventRegistrationId;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRegistrationRepository extends JpaRepository<EventRegistration, EventRegistrationId> {

    boolean existsById_UserIdAndId_EventId(Long userId, Long eventId);

    void deleteById_UserIdAndId_EventId(Long userId, Long eventId);

    @Transactional
    void deleteAllById_EventId(Long eventId);
}
