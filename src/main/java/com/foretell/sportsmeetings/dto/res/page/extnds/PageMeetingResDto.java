package com.foretell.sportsmeetings.dto.res.page.extnds;

import com.foretell.sportsmeetings.dto.res.MeetingResDto;
import com.foretell.sportsmeetings.dto.res.page.PageResDto;
import lombok.Getter;

import java.util.List;

@Getter
public class PageMeetingResDto extends PageResDto {
    private final List<MeetingResDto> meetings;

    public PageMeetingResDto(int currentPage, int totalPage, List<MeetingResDto> meetings) {
        super(currentPage, totalPage);
        this.meetings = meetings;
    }
}
