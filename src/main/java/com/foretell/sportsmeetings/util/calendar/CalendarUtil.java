package com.foretell.sportsmeetings.util.calendar;

import com.foretell.sportsmeetings.dto.req.DateTimeReqDto;

import java.util.Calendar;
import java.util.GregorianCalendar;

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
        return gregorianCalendar;
    }
}
