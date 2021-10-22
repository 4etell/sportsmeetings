package com.foretell.sportsmeetings.dto.res.page.extnds;

import com.foretell.sportsmeetings.dto.res.ProfileCommentResDto;
import com.foretell.sportsmeetings.dto.res.page.PageResDto;
import lombok.Getter;

import java.util.List;

@Getter
public class ProfileCommentPageResDto extends PageResDto {
    private final List<ProfileCommentResDto> comments;

    public ProfileCommentPageResDto(int currentPage, int totalPage, List<ProfileCommentResDto> comments) {
        super(currentPage, totalPage);
        this.comments = comments;
    }
}
