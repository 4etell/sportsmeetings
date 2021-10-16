package com.foretell.sportsmeetings.repo;

import com.foretell.sportsmeetings.model.ProfileComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileCommentRepo extends JpaRepository<ProfileComment, Long> {

    @Query(value = "SELECT * FROM profile_comments WHERE profile_comments.recipient_id = ?1", nativeQuery = true)
    Page<ProfileComment> findAllByRecipientId(Pageable pageable, Long recipientId);
}
