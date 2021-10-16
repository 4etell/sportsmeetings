package com.foretell.sportsmeetings.service;

import com.foretell.sportsmeetings.dto.req.ProfileCommentReqDto;

public interface ProfileCommentService {
    boolean create(ProfileCommentReqDto profileCommentReqDto, String username);
}
