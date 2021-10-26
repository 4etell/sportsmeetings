package com.foretell.sportsmeetings.dto.res;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class MeetingCategoryResDto {
    private final Long id;
    private final String name;
}
