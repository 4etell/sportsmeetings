package com.foretell.sportsmeetings.repo;

import com.foretell.sportsmeetings.model.RequestToJoinMeeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestToJoinMeetingRepo extends JpaRepository<RequestToJoinMeeting, Long> {
    @Query(value = "SELECT * FROM request_to_join_meeting as request " +
            "WHERE request.meeting_id = ?1 AND request.creator_id = ?2",
            nativeQuery = true)
    Optional<RequestToJoinMeeting> findByMeetingIdAndCreatorId(Long meetingId, Long creatorId);

    @Query(value = "SELECT * FROM request_to_join_meeting as request " +
            "WHERE request.meeting_id = ?1",
            nativeQuery = true)
    List<RequestToJoinMeeting> findAllByMeetingId(Long meetingId);
}
