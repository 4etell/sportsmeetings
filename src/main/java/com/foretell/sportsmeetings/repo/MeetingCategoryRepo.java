package com.foretell.sportsmeetings.repo;

import com.foretell.sportsmeetings.model.MeetingCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeetingCategoryRepo extends JpaRepository<MeetingCategory, Long> {
}
