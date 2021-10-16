package com.foretell.sportsmeetings.service;

import com.foretell.sportsmeetings.dto.req.AdminMeetingCategoryReqDto;

public interface AdminService {

    boolean setUserRoleBanned(Long userId);

    boolean deleteUserRoleBanned(Long userId);

    boolean createMeetingCategory(AdminMeetingCategoryReqDto adminMeetingCategoryReqDto);

    boolean deleteProfileCommentById(Long id);
}
