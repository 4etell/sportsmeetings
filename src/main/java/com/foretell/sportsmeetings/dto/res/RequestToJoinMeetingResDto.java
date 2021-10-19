package com.foretell.sportsmeetings.dto.res;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class RequestToJoinMeetingResDto {
    private final Long id;
    private final Long meetingId;
    private final Long userId;
    private final String description;
}
