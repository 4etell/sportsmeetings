package com.foretell.sportsmeetings.dto.res.page.extnds;

import com.foretell.sportsmeetings.dto.res.ProfileCommentResDto;
import com.foretell.sportsmeetings.dto.res.page.PageResDto;
import lombok.Getter;

import java.util.List;

@Getter
public class PageProfileCommentResDto extends PageResDto {
    private final List<ProfileCommentResDto> comments;

    public PageProfileCommentResDto(int currentPage, int totalPage, List<ProfileCommentResDto> comments) {
        super(currentPage, totalPage);
        this.comments = comments;
    }
}
