package com.foretell.sportsmeetings.dto.req;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class MeetingReqDto {
    @NotNull(message = "CategoryId cannot be null")
    private Long categoryId;

    @NotBlank(message = "Description cannot be blank")
    @Size(min = 1, max = 1000, message = "Max size of description is 1000 and min size is 1")
    private String description;

    private double latitude;

    private double longitude;

    @Valid
    @NotNull(message = "DateTimeReqDto cannot be null")
    private DateTimeReqDto startDate;

    @Valid
    @NotNull(message = "DateTimeReqDto cannot be null")
    private DateTimeReqDto endDate;

    @Min(value = 2, message = "Min value of maxNumbOfParticipants is 2")
    @Max(value = 50, message = "Max value of maxNumbOfParticipants is 50")
    private int maxNumbOfParticipants;
}
