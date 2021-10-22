package com.foretell.sportsmeetings.repo;

import com.foretell.sportsmeetings.model.Meeting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MeetingRepo extends JpaRepository<Meeting, Long> {
    @Query(value = "SELECT * FROM meetings WHERE meetings.creator_id = ?1", nativeQuery = true)
    Page<Meeting> findByCreatorId(Pageable pageable, Long id);
}
