package com.foretell.sportsmeetings.service;

import com.foretell.sportsmeetings.dto.req.ProfileCommentReqDto;
import com.foretell.sportsmeetings.dto.res.ProfileCommentPageResDto;
import com.foretell.sportsmeetings.model.ProfileComment;
import org.springframework.data.domain.Pageable;

public interface ProfileCommentService {
    boolean create(ProfileCommentReqDto profileCommentReqDto, String username);

    ProfileCommentPageResDto findAllByRecipientId(Pageable pageable, Long recipientId);

    ProfileCommentPageResDto findAllByUsername(Pageable pageable, String username);

    ProfileComment findById(Long id);

    boolean deleteCommentByIdAndAuthorUsername(Long id, String username);
}
