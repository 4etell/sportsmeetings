package com.foretell.sportsmeetings.service;

import com.foretell.sportsmeetings.dto.req.MeetingReqDto;
import com.foretell.sportsmeetings.dto.res.MeetingResDto;
import com.foretell.sportsmeetings.dto.res.page.extnds.PageMeetingResDto;
import com.foretell.sportsmeetings.model.Meeting;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MeetingService {
    MeetingResDto createMeeting(MeetingReqDto meetingReqDto, String username);

    Meeting findById(Long id);

    MeetingResDto getById(Long id);

    PageMeetingResDto getAllByCreatorUsername(Pageable pageable, String username);

    PageMeetingResDto getAllWhereParticipantNotCreatorByParticipantUsername(Pageable pageable, String username);

    PageMeetingResDto getAllByCategoryAndDistance(Pageable pageable, List<Long> categoryId, Integer distance);

    MeetingResDto addParticipantInMeeting(Long meetingId, Long participantId, String username);
}
