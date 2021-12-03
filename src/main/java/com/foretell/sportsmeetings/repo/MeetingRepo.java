package com.foretell.sportsmeetings.repo;

import com.foretell.sportsmeetings.model.Meeting;
import com.vividsolutions.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface MeetingRepo extends JpaRepository<Meeting, Long> {
    @Query(value = "SELECT * FROM meetings WHERE meetings.creator_id = ?1 AND meetings.status = ?2", nativeQuery = true)
    Page<Meeting> findAllByCreatorId(Pageable pageable, Long id, String status);

    @Query(value = "SELECT * FROM meetings WHERE meetings.creator_id = ?1 AND meetings.status = ?2", nativeQuery = true)
    List<Meeting> findAllByCreatorId(Long id, String status);

    @Query(value = "SELECT m.* FROM meetings AS m JOIN meeting_participants AS mp ON m.id = mp.meeting_id " +
            "WHERE mp.participant_id = ?1 AND m.creator_id != ?1 AND m.status = ?2",
            nativeQuery = true)
    Page<Meeting> findAllWhereParticipantNotCreatorByParticipantId(Pageable pageable, Long id, String status);

    @Query(value = "SELECT * FROM meetings " +
            "WHERE ST_DistanceSphere(CAST(geom AS geometry), CAST(:point AS geometry)) < :distanceM " +
            "AND meetings.status = :status", nativeQuery = true)
    Page<Meeting> findAllByDistance(Pageable pageable, Point point, double distanceM, String status);

    @Query(value = "SELECT m.* FROM meetings AS m WHERE m.category_id IN :categoryIds " +
            "AND ST_DistanceSphere(CAST(geom AS geometry), CAST(:point AS geometry)) < :distanceM " +
            "AND meetings.status = :status", nativeQuery = true)
    Page<Meeting> findAllByDistanceAndCategoryIds(Pageable pageable, List<Long> categoryIds, Point point, double distanceM, String status);

    @Query(value = "SELECT * FROM meetings WHERE meetings.status = ?1", nativeQuery = true)
    List<Meeting> findAllByStatus(String status);

    @Query(value = "SELECT * FROM meetings WHERE meetings.start_date > ?1", nativeQuery = true)
    List<Meeting> findAllWhichNotStarted(Date date);
}
