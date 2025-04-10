package io.github.rtheodoro4201.eventconnect.repository;

import io.github.rtheodoro4201.eventconnect.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long>{
    boolean existsByTitleAndOrganizerId(String title, Long organizerId);
}
