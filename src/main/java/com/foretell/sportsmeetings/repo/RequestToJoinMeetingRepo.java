package com.foretell.sportsmeetings.repo;

import com.foretell.sportsmeetings.model.RequestToJoinMeeting;
import com.foretell.sportsmeetings.model.RequestToJoinMeetingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RequestToJoinMeetingRepo extends JpaRepository<RequestToJoinMeeting, Long> {
    @Query(value = "SELECT r.* FROM request_to_join_meeting AS r " +
            "WHERE r.meeting_id = ?1 AND r.creator_id = ?2",
            nativeQuery = true)
    Optional<RequestToJoinMeeting> findByMeetingIdAndCreatorId(Long meetingId, Long creatorId);

    @Query(value = "SELECT r.* FROM request_to_join_meeting AS r " +
            "WHERE r.meeting_id = ?1 AND r.status = ?2",
            nativeQuery = true)
    Page<RequestToJoinMeeting> findAllWhereByMeetingIdAndStatus(Long meetingId, String status,
                                                                Pageable pageable);
}
