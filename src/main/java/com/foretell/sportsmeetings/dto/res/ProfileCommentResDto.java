package com.foretell.sportsmeetings.dto.res;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ProfileCommentResDto {
    private final Long id;
    private final Long authorId;
    private final String text;
}
