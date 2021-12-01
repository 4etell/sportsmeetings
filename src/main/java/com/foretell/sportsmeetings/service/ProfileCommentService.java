package com.foretell.sportsmeetings.service;

import com.foretell.sportsmeetings.dto.req.ProfileCommentReqDto;
import com.foretell.sportsmeetings.dto.res.page.extnds.PageProfileCommentResDto;
import com.foretell.sportsmeetings.dto.res.ProfileCommentResDto;
import com.foretell.sportsmeetings.model.ProfileComment;
import org.springframework.data.domain.Pageable;

public interface ProfileCommentService {
    ProfileComment findById(Long id);

    ProfileCommentResDto create(Long recipientId, ProfileCommentReqDto profileCommentReqDto, String username);

    PageProfileCommentResDto getAllByRecipientId(Pageable pageable, Long recipientId);

    PageProfileCommentResDto getAllByUsername(Pageable pageable, String username);

    boolean deleteCommentByIdAndAuthorUsername(Long id, String username);

    boolean deleteById(Long id);
}
