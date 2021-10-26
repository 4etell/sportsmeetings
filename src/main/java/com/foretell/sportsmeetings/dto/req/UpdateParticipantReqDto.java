package com.foretell.sportsmeetings.dto.req;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UpdateParticipantReqDto {
    @NotNull
    private Long participantId;
    private UpdateParticipantStatusReqDto updateParticipantStatusReqDto;
}
