package com.foretell.sportsmeetings.service;

import com.foretell.sportsmeetings.dto.req.RequestToJoinMeetingReqDto;
import com.foretell.sportsmeetings.dto.req.RequestToJoinMeetingStatusReqDto;
import com.foretell.sportsmeetings.dto.res.RequestToJoinMeetingResDto;
import com.foretell.sportsmeetings.dto.res.page.extnds.PageRequestToJoinMeetingResDto;
import com.foretell.sportsmeetings.model.RequestToJoinMeeting;
import org.springframework.data.domain.Pageable;

public interface RequestToJoinMeetingService {
    boolean create(Long meetingId, RequestToJoinMeetingReqDto requestToJoinMeetingReqDto, String username);

    PageRequestToJoinMeetingResDto getByMeetingId(Long meetingId, String username, Pageable pageable);

    RequestToJoinMeetingResDto updateStatus(Long requestId, Long meetingId,
                                            RequestToJoinMeetingStatusReqDto requestToJoinMeetingStatusReqDto, String username);

    RequestToJoinMeeting findById(Long id);
}
