package com.foretell.sportsmeetings.dto.res;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class MeetingResDto {
    private final Long id;

    private final Long categoryId;

    private final String description;

    private final float firstCoordinate;

    private final float secondCoordinate;

    @ApiModelProperty(example = "23.09 / 00:59")
    private final String date;

    private final int maxNumbOfParticipants;

    private final Long creatorId;

    private final List<Long> participantsIds;

}
