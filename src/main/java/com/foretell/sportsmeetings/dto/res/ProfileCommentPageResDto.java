package com.foretell.sportsmeetings.dto.res;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class ProfileCommentPageResDto {
    private final List<ProfileCommentResDto> comments;
    private final int currentPage;
    private final int totalPage;
}
