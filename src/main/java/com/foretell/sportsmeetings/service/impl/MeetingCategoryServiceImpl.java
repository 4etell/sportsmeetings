package com.foretell.sportsmeetings.service.impl;

import com.foretell.sportsmeetings.dto.res.MeetingCategoryResDto;
import com.foretell.sportsmeetings.exception.notfound.MeetingCategoryNotFoundException;
import com.foretell.sportsmeetings.model.MeetingCategory;
import com.foretell.sportsmeetings.repo.MeetingCategoryRepo;
import com.foretell.sportsmeetings.service.MeetingCategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MeetingCategoryServiceImpl implements MeetingCategoryService {

    private final MeetingCategoryRepo meetingCategoryRepo;

    public MeetingCategoryServiceImpl(MeetingCategoryRepo meetingCategoryRepo) {
        this.meetingCategoryRepo = meetingCategoryRepo;
    }

    @Override
    public List<MeetingCategoryResDto> getAll() {
        return meetingCategoryRepo.findAll().stream()
                .map(this::convertMeetingCategoryToMeetingCategoryResDto).collect(Collectors.toList());
    }

    @Override
    public MeetingCategory findById(Long id) {
        return meetingCategoryRepo.findById(id).orElseThrow(() ->
                new MeetingCategoryNotFoundException("MeetingCategory with id: " + (id) + " not found"));
    }

    @Override
    public MeetingCategoryResDto getById(Long id) {
        return convertMeetingCategoryToMeetingCategoryResDto(findById(id));
    }

    @Override
    public MeetingCategoryResDto create(MeetingCategory meetingCategory) {
        return convertMeetingCategoryToMeetingCategoryResDto(meetingCategoryRepo.save(meetingCategory));
    }

    private MeetingCategoryResDto convertMeetingCategoryToMeetingCategoryResDto(MeetingCategory meetingCategory) {
        return new MeetingCategoryResDto(
                meetingCategory.getId(),
                meetingCategory.getName()
        );
    }
}
