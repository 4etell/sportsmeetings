package com.foretell.sportsmeetings.dto.res.page.extnds;

import com.foretell.sportsmeetings.dto.res.RequestToJoinMeetingResDto;
import com.foretell.sportsmeetings.dto.res.page.PageResDto;
import lombok.Getter;

import java.util.List;

@Getter
public class PageRequestToJoinMeetingResDto extends PageResDto {
    private final List<RequestToJoinMeetingResDto> requests;

    public PageRequestToJoinMeetingResDto(int currentPage, int totalPage, List<RequestToJoinMeetingResDto> requests) {
        super(currentPage, totalPage);
        this.requests = requests;
    }
}
