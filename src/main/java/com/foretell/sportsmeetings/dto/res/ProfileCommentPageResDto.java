package com.foretell.sportsmeetings.dto.res;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ProfileCommentPageResDto {
    List<ProfileCommentResDto> comments;
    private int currentPage;
    private int totalPage;
}
