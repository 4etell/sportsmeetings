package com.foretell.sportsmeetings.service;

import com.foretell.sportsmeetings.dto.req.MeetingCategoryReqDto;
import com.foretell.sportsmeetings.dto.res.MeetingCategoryResDto;
import com.foretell.sportsmeetings.dto.res.page.extnds.PageMeetingCategoryResDto;
import com.foretell.sportsmeetings.model.MeetingCategory;
import org.springframework.data.domain.Pageable;

public interface MeetingCategoryService {
    PageMeetingCategoryResDto getAll(Pageable pageable);

    MeetingCategory findById(Long id);

    MeetingCategoryResDto getById(Long id);

    MeetingCategoryResDto create(MeetingCategoryReqDto meetingCategoryReqDto);

}
