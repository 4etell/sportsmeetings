package com.foretell.sportsmeetings.service.impl;

import com.foretell.sportsmeetings.dto.req.RequestToJoinMeetingReqDto;
import com.foretell.sportsmeetings.dto.res.RequestToJoinMeetingResDto;
import com.foretell.sportsmeetings.exception.RequestToJoinMeetingException;
import com.foretell.sportsmeetings.model.Meeting;
import com.foretell.sportsmeetings.model.RequestToJoinMeeting;
import com.foretell.sportsmeetings.model.RequestToJoinMeetingStatus;
import com.foretell.sportsmeetings.model.User;
import com.foretell.sportsmeetings.repo.RequestToJoinMeetingRepo;
import com.foretell.sportsmeetings.service.MeetingService;
import com.foretell.sportsmeetings.service.RequestToJoinMeetingService;
import com.foretell.sportsmeetings.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RequestToJoinMeetingServiceImpl implements RequestToJoinMeetingService {
    private final RequestToJoinMeetingRepo requestToJoinMeetingRepo;
    private final UserService userService;
    private final MeetingService meetingService;

    public RequestToJoinMeetingServiceImpl(RequestToJoinMeetingRepo requestToJoinMeetingRepo, UserService userService, MeetingService meetingService) {
        this.requestToJoinMeetingRepo = requestToJoinMeetingRepo;
        this.userService = userService;
        this.meetingService = meetingService;
    }


    @Override
    public boolean create(RequestToJoinMeetingReqDto requestToJoinMeetingReqDto, String username) {
        User user = userService.findByUsername(username);
        Long userId = user.getId();
        Long meetingId = requestToJoinMeetingReqDto.getMeetingId();
        if (requestToJoinMeetingRepo.findByMeetingIdAndCreatorId(meetingId, userId).isEmpty()) {
            Meeting meeting = meetingService.findById(meetingId);

            if (meeting.getCreator().getId().equals(userId)) {
                throw new RequestToJoinMeetingException("You cannot create request to your meeting");
            }

            RequestToJoinMeeting requestToJoinMeeting = new RequestToJoinMeeting(
                    requestToJoinMeetingReqDto.getDescription(),
                    user,
                    meeting,
                    RequestToJoinMeetingStatus.CREATED
            );
            requestToJoinMeetingRepo.save(requestToJoinMeeting);
            return true;
        } else {
            throw new RequestToJoinMeetingException("You already created request to this meeting");
        }
    }

    @Override
    public List<RequestToJoinMeetingResDto> getByMeetingId(Long meetingId, String username) {
        User user = userService.findByUsername(username);
        Meeting meeting = meetingService.findById(meetingId);
        if (meeting.getCreator().getId().equals(user.getId())) {
            return requestToJoinMeetingRepo.findAllByMeetingId(meeting.getId())
                    .stream()
                    .map(this::convertRequestToJoinMeetingToRequestToJoinMeetingResDto)
                    .collect(Collectors.toList());
        } else {
            throw new RequestToJoinMeetingException("You can get requests only for your meeting");
        }
    }

    private RequestToJoinMeetingResDto convertRequestToJoinMeetingToRequestToJoinMeetingResDto(
            RequestToJoinMeeting requestToJoinMeeting) {
        return new RequestToJoinMeetingResDto(
                requestToJoinMeeting.getId(),
                requestToJoinMeeting.getMeeting().getId(),
                requestToJoinMeeting.getCreator().getId(),
                requestToJoinMeeting.getDescription());
    }

}
