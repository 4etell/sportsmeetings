package com.foretell.sportsmeetings.service.impl;

import com.foretell.sportsmeetings.dto.req.DateTimeReqDto;
import com.foretell.sportsmeetings.dto.req.MeetingReqDto;
import com.foretell.sportsmeetings.dto.req.UpdateParticipantReqDto;
import com.foretell.sportsmeetings.dto.req.UpdateParticipantStatusReqDto;
import com.foretell.sportsmeetings.dto.res.MeetingResDto;
import com.foretell.sportsmeetings.dto.res.page.extnds.PageMeetingResDto;
import com.foretell.sportsmeetings.exception.InvalidDateTimeReqDtoException;
import com.foretell.sportsmeetings.exception.UpdateParticipantsException;
import com.foretell.sportsmeetings.exception.UserHaveNotPermissionException;
import com.foretell.sportsmeetings.exception.notfound.MeetingNotFoundException;
import com.foretell.sportsmeetings.model.Meeting;
import com.foretell.sportsmeetings.model.MeetingCategory;
import com.foretell.sportsmeetings.model.User;
import com.foretell.sportsmeetings.repo.MeetingRepo;
import com.foretell.sportsmeetings.service.MeetingCategoryService;
import com.foretell.sportsmeetings.service.MeetingService;
import com.foretell.sportsmeetings.service.UserService;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MeetingServiceImpl implements MeetingService {

    private final GeometryFactory geoFactory = new GeometryFactory(new PrecisionModel(), 4326);

    private final UserService userService;
    private final MeetingRepo meetingRepo;
    private final MeetingCategoryService meetingCategoryService;

    public MeetingServiceImpl(UserService userService, MeetingRepo meetingRepo, MeetingCategoryService meetingCategoryService) {
        this.userService = userService;
        this.meetingRepo = meetingRepo;
        this.meetingCategoryService = meetingCategoryService;
    }

    @Override
    public MeetingResDto createMeeting(MeetingReqDto meetingReqDto, String username) {
        User user = userService.findByUsername(username);
        MeetingCategory meetingCategory = meetingCategoryService.findById(meetingReqDto.getCategoryId());
        GregorianCalendar gregorianCalendarToMeeting = createGregorianCalendarToMeeting(meetingReqDto.getDateTimeReqDto());
        Set<User> participants = new HashSet<>();
        Point point = geoFactory.createPoint(
                new Coordinate(meetingReqDto.getFirstCoordinate(), meetingReqDto.getSecondCoordinate()));
        participants.add(user);
        Meeting meeting = new Meeting(
                meetingCategory,
                meetingReqDto.getDescription(),
                point,
                gregorianCalendarToMeeting,
                meetingReqDto.getMaxNumbOfParticipants(),
                user,
                participants
        );
        return convertMeetingToMeetingResDto(meetingRepo.save(meeting));
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
    public PageMeetingResDto getAllByCreatorUsername(Pageable pageable, String username) {
        User user = userService.findByUsername(username);
        Page<Meeting> page = meetingRepo.findAllByCreatorId(pageable, user.getId());
        return convertMeetingPageToPageMeetingResDto(page, pageable);
    }

    @Override
    public PageMeetingResDto getAllWhereParticipantNotCreatorByParticipantUsername(Pageable pageable, String username) {
        User user = userService.findByUsername(username);
        Page<Meeting> page = meetingRepo.findAllWhereParticipantNotCreatorByParticipantId(pageable, user.getId());
        return convertMeetingPageToPageMeetingResDto(page, pageable);
    }

    @Override
    public PageMeetingResDto getAllByCategoryAndDistance(Pageable pageable, List<Long> categoryIds, double userFirstCord, double userSecondCord, int distance) {
        Page<Meeting> page;
        Point point = geoFactory.createPoint(
                new Coordinate(userFirstCord, userSecondCord));
        if (categoryIds == null) {
            page = meetingRepo.findAllByDistance(pageable, point, distance);
        } else {
            page = meetingRepo.findAllByDistanceAndCategoryIds(pageable, categoryIds, point, distance);
        }
        return convertMeetingPageToPageMeetingResDto(page, pageable);
    }

    @Override
    public MeetingResDto updateParticipantsInMeeting(Long meetingId, UpdateParticipantReqDto updateParticipantReqDto, String username) {
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


    private GregorianCalendar createGregorianCalendarToMeeting(DateTimeReqDto dateTimeReqDto) {
        final long maxTimeToCreateInMs = 1_209_600_000;

        Calendar calendar = Calendar.getInstance();
        final int currentYear = calendar.get(Calendar.YEAR);
        final int currentMonth = calendar.get(Calendar.MONTH);
        final int monthFromDto = dateTimeReqDto.getMonth();
        GregorianCalendar gregorianCalendar =
                new GregorianCalendar(
                        currentYear,
                        monthFromDto - 1,
                        dateTimeReqDto.getDayOfMonth(),
                        dateTimeReqDto.getHourOfDay(),
                        dateTimeReqDto.getMinute());
        if (monthFromDto == 1 && currentMonth == 11) {
            gregorianCalendar.set(Calendar.YEAR, currentYear + 1);
        }
        final long currentTimeInMs = calendar.getTimeInMillis();
        final long gregorianCalendarTimeInMs = gregorianCalendar.getTimeInMillis();
        if ((currentTimeInMs < gregorianCalendarTimeInMs) &&
                ((currentTimeInMs + maxTimeToCreateInMs) >= gregorianCalendarTimeInMs)) {
            return gregorianCalendar;
        } else {
            throw new InvalidDateTimeReqDtoException("Date time is invalid");
        }
    }


    private String convertDateOfMeetingToString(GregorianCalendar gregorianCalendar) {
        int calendarDay = gregorianCalendar.get(Calendar.DAY_OF_MONTH);
        int calendarMonth = gregorianCalendar.get(Calendar.MONTH) + 1;
        int calendarHour = gregorianCalendar.get(Calendar.HOUR_OF_DAY);
        int calendarMinute = gregorianCalendar.get(Calendar.MINUTE);

        String day = calendarDay < 10 ? "0" + calendarDay : String.valueOf(calendarDay);
        String month = calendarMonth < 10 ? "0" + calendarMonth : String.valueOf(calendarMonth);
        String hour = calendarHour < 10 ? "0" + calendarHour : String.valueOf(calendarHour);
        String minute = calendarMinute < 10 ? "0" + calendarMinute : String.valueOf(calendarMinute);

        return day + "." + month + " / " + hour + ":" + minute;
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
                convertDateOfMeetingToString(meeting.getDate()),
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
