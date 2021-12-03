package com.foretell.sportsmeetings.service;

import com.foretell.sportsmeetings.dto.req.MeetingReqDto;
import com.foretell.sportsmeetings.dto.req.UpdateParticipantReqDto;
import com.foretell.sportsmeetings.dto.res.MeetingResDto;
import com.foretell.sportsmeetings.dto.res.page.extnds.PageMeetingResDto;
import com.foretell.sportsmeetings.model.Meeting;
import com.foretell.sportsmeetings.model.MeetingStatus;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MeetingService {
    MeetingResDto createMeeting(MeetingReqDto meetingReqDto, String username);

    Meeting findById(Long id);

    MeetingResDto getById(Long id);

    PageMeetingResDto getAllByCreatorUsername(Pageable pageable, String username, MeetingStatus meetingStatus);

    PageMeetingResDto getAllWhereParticipantNotCreatorByParticipantUsername(Pageable pageable, String username, MeetingStatus meetingStatus);

    PageMeetingResDto getAllByCategoryAndDistance(Pageable pageable, List<Long> categoryId, double userLatitude, double longitude, int distance);

    MeetingResDto updateParticipantsInMeeting(Long meetingId, UpdateParticipantReqDto updateParticipantReqDto, String username);

    boolean updateStatus(Long meetingId, MeetingStatus meetingStatus);

    List<Meeting> findAllExpiredMeetings();

    List<Meeting> findAllMeetingsWhichNotStarted();

}
