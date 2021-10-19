package com.foretell.sportsmeetings.dto.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class RequestToJoinMeetingReqDto {
    @NotNull(message = "MeetingId cannot be null")
    private Long meetingId;

    @NotBlank(message = "Description cannot be blank")
    @Size(max = 256, message = "Max size of description is 256")
    private String description;
}
