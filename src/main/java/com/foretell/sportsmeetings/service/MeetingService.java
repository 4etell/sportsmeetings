package com.foretell.sportsmeetings.service;

import com.foretell.sportsmeetings.dto.req.MeetingReqDto;
import com.foretell.sportsmeetings.dto.res.MeetingResDto;
import com.foretell.sportsmeetings.model.Meeting;

public interface MeetingService {
    MeetingResDto createMeeting(MeetingReqDto meetingReqDto, String username);

    Meeting findById(Long id);
}
