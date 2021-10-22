package com.foretell.sportsmeetings.service;

import com.foretell.sportsmeetings.dto.req.MeetingReqDto;
import com.foretell.sportsmeetings.dto.res.MeetingResDto;
import com.foretell.sportsmeetings.dto.res.page.extnds.PageMeetingResDto;
import com.foretell.sportsmeetings.model.Meeting;
import org.springframework.data.domain.Pageable;

public interface MeetingService {
    MeetingResDto createMeeting(MeetingReqDto meetingReqDto, String username);

    Meeting findById(Long id);

    MeetingResDto getById(Long id);

    PageMeetingResDto getAllByCreatorUsername(Pageable pageable, String username);
}
