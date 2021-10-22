package com.foretell.sportsmeetings.service;

import com.foretell.sportsmeetings.dto.req.RequestToJoinMeetingReqDto;
import com.foretell.sportsmeetings.dto.req.RequestToJoinMeetingStatusReqDto;
import com.foretell.sportsmeetings.dto.res.RequestToJoinMeetingResDto;
import com.foretell.sportsmeetings.model.RequestToJoinMeeting;

import java.util.List;

public interface RequestToJoinMeetingService {
    boolean create(RequestToJoinMeetingReqDto requestToJoinMeetingReqDto, String username);

    List<RequestToJoinMeetingResDto> getByMeetingId(Long meetingId, String username);

    RequestToJoinMeetingResDto updateStatus(Long id,
                                            RequestToJoinMeetingStatusReqDto requestToJoinMeetingStatusReqDto,
                                            String meetingCreatorUsername);

    RequestToJoinMeeting findById(Long id);
}
