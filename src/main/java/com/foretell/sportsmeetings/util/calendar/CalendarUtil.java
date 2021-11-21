package com.foretell.sportsmeetings.util.calendar;

import com.foretell.sportsmeetings.dto.req.DateTimeReqDto;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class CalendarUtil {
    public static GregorianCalendar createGregorianCalendarByDateTimeReqDto(DateTimeReqDto dateTimeReqDto) {
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
        ZoneOffset zoneOffset =
                ZoneOffset.ofTotalSeconds(-dateTimeReqDto.getTimeZoneOffset() * 60);
        gregorianCalendar.setTimeZone(TimeZone.getTimeZone(zoneOffset));
        System.out.println(TimeZone.getTimeZone(zoneOffset));
        return gregorianCalendar;
    }

    public static String convertDateOfMeetingToString(GregorianCalendar gregorianCalendar) {
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
}
