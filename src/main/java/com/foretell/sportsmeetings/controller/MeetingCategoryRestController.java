package com.foretell.sportsmeetings.controller;

import com.foretell.sportsmeetings.dto.res.MeetingCategoryResDto;
import com.foretell.sportsmeetings.service.MeetingCategoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("meeting-categories")
public class MeetingCategoryRestController {

    private final MeetingCategoryService meetingCategoryService;

    public MeetingCategoryRestController(MeetingCategoryService meetingCategoryService) {
        this.meetingCategoryService = meetingCategoryService;
    }

    @GetMapping
    public List<MeetingCategoryResDto> getAll() {
        return meetingCategoryService.getAll();
    }

    @GetMapping("{id}")
    public MeetingCategoryResDto getById(@PathVariable Long id) {
        return meetingCategoryService.getById(id);
    }
}
