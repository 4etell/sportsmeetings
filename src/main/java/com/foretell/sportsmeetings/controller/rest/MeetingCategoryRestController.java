package com.foretell.sportsmeetings.controller.rest;

import com.foretell.sportsmeetings.dto.res.MeetingCategoryResDto;
import com.foretell.sportsmeetings.dto.res.page.extnds.PageMeetingCategoryResDto;
import com.foretell.sportsmeetings.service.MeetingCategoryService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("meeting-categories")
public class MeetingCategoryRestController {

    private final MeetingCategoryService meetingCategoryService;

    public MeetingCategoryRestController(MeetingCategoryService meetingCategoryService) {
        this.meetingCategoryService = meetingCategoryService;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", paramType = "query",
                    dataTypeClass = Long.class,
                    value = "Results page you want to retrieve (0..N)"),
    })
    @GetMapping
    public PageMeetingCategoryResDto getAll(
            @PageableDefault(size = 20, sort = {"name"}) Pageable pageable) {
        return meetingCategoryService.getAll(pageable);
    }

    @GetMapping("{id}")
    public MeetingCategoryResDto getById(@PathVariable Long id) {
        return meetingCategoryService.getById(id);
    }
}
