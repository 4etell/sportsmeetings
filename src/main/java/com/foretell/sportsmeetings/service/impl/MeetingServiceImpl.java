package com.foretell.sportsmeetings.service.impl;

import com.foretell.sportsmeetings.dto.req.DateTimeReqDto;
import com.foretell.sportsmeetings.dto.req.MeetingReqDto;
import com.foretell.sportsmeetings.dto.req.UpdateParticipantReqDto;
import com.foretell.sportsmeetings.dto.req.UpdateParticipantStatusReqDto;
import com.foretell.sportsmeetings.dto.res.MeetingResDto;
import com.foretell.sportsmeetings.dto.res.page.extnds.PageMeetingResDto;
import com.foretell.sportsmeetings.exception.InvalidDateTimeReqDtoException;
import com.foretell.sportsmeetings.exception.MaxCountOfMeetingsException;
import com.foretell.sportsmeetings.exception.UpdateParticipantsException;
import com.foretell.sportsmeetings.exception.UserHaveNotPermissionException;
import com.foretell.sportsmeetings.exception.notfound.MeetingNotFoundException;
import com.foretell.sportsmeetings.model.Meeting;
import com.foretell.sportsmeetings.model.MeetingCategory;
import com.foretell.sportsmeetings.model.MeetingStatus;
import com.foretell.sportsmeetings.model.User;
import com.foretell.sportsmeetings.repo.MeetingRepo;
import com.foretell.sportsmeetings.service.MeetingCategoryService;
import com.foretell.sportsmeetings.service.MeetingService;
import com.foretell.sportsmeetings.service.UserService;
import com.foretell.sportsmeetings.util.calendar.CalendarUtil;
import com.foretell.sportsmeetings.util.scheduler.CustomScheduler;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MeetingServiceImpl implements MeetingService {

    private final GeometryFactory geoFactory = new GeometryFactory(new PrecisionModel(), 4326);

    private final UserService userService;
    private final MeetingRepo meetingRepo;
    private final MeetingCategoryService meetingCategoryService;
    private final CustomScheduler customScheduler;

    public MeetingServiceImpl(UserService userService, MeetingRepo meetingRepo, MeetingCategoryService meetingCategoryService, CustomScheduler customScheduler) {
        this.userService = userService;
        this.meetingRepo = meetingRepo;
        this.meetingCategoryService = meetingCategoryService;
        this.customScheduler = customScheduler;
    }

    @Override
    public MeetingResDto createMeeting(MeetingReqDto meetingReqDto, String username) {
        User user = userService.findByUsername(username);
        MeetingCategory meetingCategory = meetingCategoryService.findById(meetingReqDto.getCategoryId());
        if (meetingRepo.findAllByCreatorId(user.getId(), MeetingStatus.CREATED.toString()).size() < 10) {
            GregorianCalendar startDate = createStartDateOfMeeting(meetingReqDto.getStartDate());
            GregorianCalendar endDate = createEndDateOfMeeting(startDate, meetingReqDto.getEndDate());
            Set<User> participants = new HashSet<>();
            Point point = geoFactory.createPoint(
                    new Coordinate(meetingReqDto.getLatitude(), meetingReqDto.getLongitude()));
            participants.add(user);
            Meeting meeting = new Meeting(
                    meetingCategory,
                    MeetingStatus.CREATED,
                    meetingReqDto.getDescription(),
                    point, startDate,
                    endDate,
                    meetingReqDto.getMaxNumbOfParticipants(),
                    user,
                    participants
            );

            Meeting savedMeeting = meetingRepo.save(meeting);
            customScheduler.scheduleMeetingFinishedStatusTask(savedMeeting.getId(), savedMeeting.getEndDate().getTime());
            customScheduler.scheduleTelegramStartMeetingNotification(savedMeeting.getId(), savedMeeting.getEndDate().getTimeInMillis() - 7200000);

            return convertMeetingToMeetingResDto(savedMeeting);
        } else {
            throw  new MaxCountOfMeetingsException("Max count of meetings with status CREATED is 10");
        }
    }

    @Override
    public Meeting findById(Long id) {
        return meetingRepo.findById(id)
                .orElseThrow(() -> new MeetingNotFoundException("Meeting with id: " + (id) + " not found"));
    }

    @Override
    public MeetingResDto getById(Long id) {
        return convertMeetingToMeetingResDto(findById(id));
    }

    @Override
    public PageMeetingResDto getAllByCreatorUsername(Pageable pageable, String username, MeetingStatus meetingStatus) {
        User user = userService.findByUsername(username);
        Page<Meeting> page = meetingRepo.findAllByCreatorId(pageable, user.getId(), meetingStatus.toString());
        return convertMeetingPageToPageMeetingResDto(page, pageable);
    }

    @Override
    public PageMeetingResDto getAllWhereParticipantNotCreatorByParticipantUsername(Pageable pageable, String
            username, MeetingStatus meetingStatus) {
        User user = userService.findByUsername(username);
        Page<Meeting> page = meetingRepo.findAllWhereParticipantNotCreatorByParticipantId(pageable, user.getId(), meetingStatus.toString());
        return convertMeetingPageToPageMeetingResDto(page, pageable);
    }

    @Override
    public PageMeetingResDto getAllByCategoryAndDistance(Pageable pageable, List<Long> categoryIds,
                                                         double userLatitude, double longitude, int distance) {
        Page<Meeting> page;
        Point point = geoFactory.createPoint(
                new Coordinate(userLatitude, longitude));
        if (categoryIds == null) {
            page = meetingRepo.findAllByDistance(pageable, point, distance, MeetingStatus.CREATED.toString());
        } else {
            page = meetingRepo.findAllByDistanceAndCategoryIds(pageable, categoryIds, point, distance, MeetingStatus.CREATED.toString());
        }
        return convertMeetingPageToPageMeetingResDto(page, pageable);
    }

    @Override
    public MeetingResDto updateParticipantsInMeeting(Long meetingId, UpdateParticipantReqDto
            updateParticipantReqDto, String username) {
        Meeting meeting = findById(meetingId);
        User user = userService.findByUsername(username);
        Long participantId = updateParticipantReqDto.getParticipantId();
        UpdateParticipantStatusReqDto statusToUpdate = updateParticipantReqDto.getUpdateParticipantStatusReqDto();
        User participant = userService.findById(participantId);
        boolean isUpdated = false;
        boolean isRemove = statusToUpdate == UpdateParticipantStatusReqDto.REMOVE;
        boolean isAdd = statusToUpdate == UpdateParticipantStatusReqDto.ADD;
        boolean isCreator = meeting.getCreator().getId().equals(user.getId());
        if (isCreator && isAdd) {
            isUpdated = meeting.addParticipant(participant);
        } else if (isRemove && (isCreator || (meeting.isParticipant(user) && user.getId().equals(participantId)))) {
            isUpdated = meeting.removeParticipant(participant);
        } else {
            throw new UserHaveNotPermissionException("You have not permission");
        }
        if (isUpdated) {
            Meeting updatedMeeting = meetingRepo.save(meeting);
            return convertMeetingToMeetingResDto(updatedMeeting);
        } else {

            throw new UpdateParticipantsException("Server cannot add/remove this participantId: " + participantId);
        }

    }

    @Override
    public boolean updateStatus(Long meetingId, MeetingStatus meetingStatus) {
        Meeting meeting = findById(meetingId);
        meeting.setStatus(meetingStatus);
        meetingRepo.save(meeting);
        return true;
    }

    @Override
    public List<Meeting> findAllExpiredMeetings() {
        return meetingRepo.findAllByStatus(MeetingStatus.CREATED.toString());
    }

    @Override
    public List<Meeting> findAllMeetingsWhichNotStarted() {
        return meetingRepo.findAllWhichNotStarted(new Date());
    }


    private GregorianCalendar createStartDateOfMeeting(DateTimeReqDto dateTimeReqDto) {
        final long maxTimeToCreateInMs = 1_209_600_000;
        GregorianCalendar startDate = CalendarUtil.createGregorianCalendarByDateTimeReqDto(dateTimeReqDto);

        final long currentTimeInMs = new Date().getTime();
        final long gregorianCalendarTimeInMs = startDate.getTimeInMillis();
        if ((currentTimeInMs < gregorianCalendarTimeInMs) &&
                ((currentTimeInMs + maxTimeToCreateInMs) >= gregorianCalendarTimeInMs)) {
            return startDate;
        } else {
            throw new InvalidDateTimeReqDtoException("Meetings can only be created 2 weeks in advance");
        }
    }

    private GregorianCalendar createEndDateOfMeeting(GregorianCalendar startDate, DateTimeReqDto endDateReqDto) {
        GregorianCalendar endDate = CalendarUtil.createGregorianCalendarByDateTimeReqDto(endDateReqDto);
        if (endDate.getTimeInMillis() > startDate.getTimeInMillis()) {
            return endDate;
        } else {
            throw new InvalidDateTimeReqDtoException("Invalid endDate");
        }
    }

    private MeetingResDto convertMeetingToMeetingResDto(Meeting meeting) {
        List<Long> participantsIds = new ArrayList<>();
        meeting.getParticipants().forEach(user -> participantsIds.add(user.getId()));
        return new MeetingResDto(
                meeting.getId(),
                meeting.getCategory().getId(),
                meeting.getDescription(),
                meeting.getGeom().getCoordinate().x,
                meeting.getGeom().getCoordinate().y,
                CalendarUtil.convertDateOfMeetingToString(meeting.getStartDate()),
                CalendarUtil.convertDateOfMeetingToString(meeting.getEndDate()),
                meeting.getMaxNumbOfParticipants(),
                meeting.getCreator().getId(),
                participantsIds
        );
    }

    private PageMeetingResDto convertMeetingPageToPageMeetingResDto(Page<Meeting> page, Pageable pageable) {
        List<MeetingResDto> meetingResDtoList =
                page.getContent().stream()
                        .map(this::convertMeetingToMeetingResDto)
                        .collect(Collectors.toList());

        return new PageMeetingResDto(
                pageable.getPageNumber(),
                page.getTotalPages(),
                meetingResDtoList
        );
    }

}
