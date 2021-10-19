package com.foretell.sportsmeetings.service.impl;

import com.foretell.sportsmeetings.dto.req.DateTimeReqDto;
import com.foretell.sportsmeetings.dto.req.MeetingReqDto;
import com.foretell.sportsmeetings.exception.InvalidDateTimeReqDtoException;
import com.foretell.sportsmeetings.exception.MeetingNotFoundException;
import com.foretell.sportsmeetings.model.Meeting;
import com.foretell.sportsmeetings.model.MeetingCategory;
import com.foretell.sportsmeetings.model.User;
import com.foretell.sportsmeetings.repo.MeetingRepo;
import com.foretell.sportsmeetings.service.MeetingCategoryService;
import com.foretell.sportsmeetings.service.MeetingService;
import com.foretell.sportsmeetings.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.GregorianCalendar;

@Service
public class MeetingServiceImpl implements MeetingService {

    private final UserService userService;
    private final MeetingRepo meetingRepo;
    private final MeetingCategoryService meetingCategoryService;

    public MeetingServiceImpl(UserService userService, MeetingRepo meetingRepo, MeetingCategoryService meetingCategoryService) {
        this.userService = userService;
        this.meetingRepo = meetingRepo;
        this.meetingCategoryService = meetingCategoryService;
    }

    @Override
    public boolean createMeeting(MeetingReqDto meetingReqDto, String username) {
        User user = userService.findByUsername(username);
        MeetingCategory meetingCategory = meetingCategoryService.findById(meetingReqDto.getCategoryId());
        GregorianCalendar gregorianCalendarToMeeting = createGregorianCalendarToMeeting(meetingReqDto.getDateTimeReqDto());
        Meeting meeting = new Meeting(
                meetingCategory,
                meetingReqDto.getDescription(),
                meetingReqDto.getFirstCoordinate(),
                meetingReqDto.getSecondCoordinate(),
                gregorianCalendarToMeeting,
                meetingReqDto.getMaxNumbOfParticipants(),
                user,
                null
        );
        meetingRepo.save(meeting);
        return true;
    }

    @Override
    public Meeting findById(Long id) {
        return meetingRepo.findById(id)
                .orElseThrow(() -> new MeetingNotFoundException("Meeting with id: " + (id) + " not found"));
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
}
