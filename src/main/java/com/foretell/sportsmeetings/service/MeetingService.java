package com.foretell.sportsmeetings.service;

import com.foretell.sportsmeetings.dto.req.MeetingReqDto;

public interface MeetingService {
    boolean createMeeting(MeetingReqDto meetingReqDto, String username);

}
