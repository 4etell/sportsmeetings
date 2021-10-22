package com.foretell.sportsmeetings.service;

import com.foretell.sportsmeetings.dto.req.ProfileCommentReqDto;
import com.foretell.sportsmeetings.dto.res.page.extnds.ProfileCommentPageResDto;
import com.foretell.sportsmeetings.dto.res.ProfileCommentResDto;
import com.foretell.sportsmeetings.model.ProfileComment;
import org.springframework.data.domain.Pageable;

public interface ProfileCommentService {
    ProfileComment findById(Long id);

    ProfileCommentResDto create(ProfileCommentReqDto profileCommentReqDto, String username);

    ProfileCommentPageResDto getAllByRecipientId(Pageable pageable, Long recipientId);

    ProfileCommentPageResDto getAllByUsername(Pageable pageable, String username);

    boolean deleteCommentByIdAndAuthorUsername(Long id, String username);

    boolean deleteById(Long id);
}
