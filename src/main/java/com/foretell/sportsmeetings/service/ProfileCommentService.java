package com.foretell.sportsmeetings.service;

import com.foretell.sportsmeetings.dto.req.ProfileCommentReqDto;
import com.foretell.sportsmeetings.dto.res.ProfileCommentPageResDto;
import com.foretell.sportsmeetings.model.ProfileComment;
import org.springframework.data.domain.Pageable;

public interface ProfileCommentService {
    ProfileComment findById(Long id);

    boolean create(ProfileCommentReqDto profileCommentReqDto, String username);

    ProfileCommentPageResDto getAllByRecipientId(Pageable pageable, Long recipientId);

    ProfileCommentPageResDto getAllByUsername(Pageable pageable, String username);

    boolean deleteCommentByIdAndAuthorUsername(Long id, String username);
}
