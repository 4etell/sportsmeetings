package com.foretell.sportsmeetings.service.impl;

import com.foretell.sportsmeetings.dto.req.MeetingCategoryReqDto;
import com.foretell.sportsmeetings.dto.res.MeetingCategoryResDto;
import com.foretell.sportsmeetings.dto.res.page.extnds.PageMeetingCategoryResDto;
import com.foretell.sportsmeetings.exception.notfound.MeetingCategoryNotFoundException;
import com.foretell.sportsmeetings.model.MeetingCategory;
import com.foretell.sportsmeetings.repo.MeetingCategoryRepo;
import com.foretell.sportsmeetings.service.MeetingCategoryService;
import com.foretell.sportsmeetings.util.string.NamingUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public PageMeetingCategoryResDto getAll(Pageable pageable) {
        Page<MeetingCategory> page = meetingCategoryRepo.findAll(pageable);
        return convertMeetingCategoryPageToPageMeetingCategoryResDto(page, pageable);
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
    public MeetingCategoryResDto create(MeetingCategoryReqDto meetingCategoryReqDto) {
        MeetingCategory meetingCategory = new MeetingCategory();
        meetingCategory.setName(NamingUtil.generateCapitalizedString(meetingCategoryReqDto.getName()));
        return convertMeetingCategoryToMeetingCategoryResDto(meetingCategoryRepo.save(meetingCategory));
    }

    private MeetingCategoryResDto convertMeetingCategoryToMeetingCategoryResDto(MeetingCategory meetingCategory) {
        return new MeetingCategoryResDto(
                meetingCategory.getId(),
                meetingCategory.getName()
        );
    }

    private PageMeetingCategoryResDto convertMeetingCategoryPageToPageMeetingCategoryResDto(Page<MeetingCategory> page,
                                                                                            Pageable pageable) {

        List<MeetingCategoryResDto> meetingCategoryResDtoList =
                page.getContent().stream()
                        .map(this::convertMeetingCategoryToMeetingCategoryResDto)
                        .collect(Collectors.toList());

        return new PageMeetingCategoryResDto(
                pageable.getPageNumber(),
                page.getTotalPages(),
                meetingCategoryResDtoList
        );
    }
}
