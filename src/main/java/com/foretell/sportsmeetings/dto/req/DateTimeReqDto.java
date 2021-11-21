package com.foretell.sportsmeetings.dto.req;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@RequiredArgsConstructor
public class DateTimeReqDto {
    @Min(value = 1, message = "Min value of month is 1")
    @Max(value = 12, message = "Max value of month is 12")
    private final int month;
    @Min(value = 1, message = "Min value of dayOfMonth is 1")
    @Max(value = 31, message = "Max value of dayOfMonth is 31")
    private final int dayOfMonth;
    @Min(value = 0, message = "Min value of hourOfDay is 0")
    @Max(value = 23, message = "Max value of hourOfDay is 23")
    private final int hourOfDay;
    @Min(value = 0, message = "Min value of minuteOfDay is 0")
    @Max(value = 59, message = "Max value of minuteOfDay is 59")
    private final int minute;
    @NotNull
    private final int timeZoneOffset;
}
