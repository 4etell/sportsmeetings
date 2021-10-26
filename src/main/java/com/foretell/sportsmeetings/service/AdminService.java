package com.foretell.sportsmeetings.service;

import com.foretell.sportsmeetings.dto.req.AdminMeetingCategoryReqDto;
import com.foretell.sportsmeetings.dto.res.MeetingCategoryResDto;

public interface AdminService {

    boolean setUserRoleBanned(Long userId);

    boolean deleteUserRoleBanned(Long userId);

    MeetingCategoryResDto createMeetingCategory(AdminMeetingCategoryReqDto adminMeetingCategoryReqDto);

    boolean deleteProfileCommentById(Long id);
}
