package com.foretell.sportsmeetings.dto.res.page.extnds;

import com.foretell.sportsmeetings.dto.res.MeetingCategoryResDto;
import com.foretell.sportsmeetings.dto.res.page.PageResDto;
import lombok.Getter;

import java.util.List;

@Getter
public class PageMeetingCategoryResDto extends PageResDto {
    private final List<MeetingCategoryResDto> categories;

    public PageMeetingCategoryResDto(int currentPage, int totalPage, List<MeetingCategoryResDto> categories) {
        super(currentPage, totalPage);
        this.categories = categories;
    }
}
