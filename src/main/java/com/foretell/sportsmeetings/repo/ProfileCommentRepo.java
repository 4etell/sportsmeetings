package com.foretell.sportsmeetings.repo;

import com.foretell.sportsmeetings.model.ProfileComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileCommentRepo extends JpaRepository<ProfileComment, Long> {

}
