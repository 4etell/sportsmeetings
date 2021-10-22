package com.foretell.sportsmeetings.dto.req;

import com.foretell.sportsmeetings.model.RequestToJoinMeetingStatus;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RequestToJoinMeetingStatusReqDto {
    @NotNull
    private RequestToJoinMeetingStatus requestToJoinMeetingStatus;
}
