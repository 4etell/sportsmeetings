package com.foretell.sportsmeetings.service;

import com.foretell.sportsmeetings.dto.res.MeetingCategoryResDto;
import com.foretell.sportsmeetings.model.MeetingCategory;

import java.util.List;

public interface MeetingCategoryService {
    List<MeetingCategoryResDto> getAll();
    MeetingCategory findById(Long id);
    MeetingCategoryResDto getById(Long id);

}
