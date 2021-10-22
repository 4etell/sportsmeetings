package com.foretell.sportsmeetings.repo;

import com.foretell.sportsmeetings.model.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeetingRepo extends JpaRepository<Meeting, Long> {
}
