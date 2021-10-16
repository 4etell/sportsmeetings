package com.foretell.sportsmeetings.service.impl;

import com.foretell.sportsmeetings.dto.req.ProfileCommentReqDto;
import com.foretell.sportsmeetings.exception.ProfileCommentException;
import com.foretell.sportsmeetings.model.ProfileComment;
import com.foretell.sportsmeetings.model.User;
import com.foretell.sportsmeetings.repo.ProfileCommentRepo;
import com.foretell.sportsmeetings.service.ProfileCommentService;
import com.foretell.sportsmeetings.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class ProfileCommentServiceImpl implements ProfileCommentService {

    private final UserService userService;
    private final ProfileCommentRepo profileCommentRepo;

    public ProfileCommentServiceImpl(UserService userService, ProfileCommentRepo profileCommentRepo) {
        this.userService = userService;
        this.profileCommentRepo = profileCommentRepo;
    }

    @Override
    public boolean create(ProfileCommentReqDto profileCommentReqDto, String username) {
        User author = userService.findByUsername(username);
        User recipient = userService.findById(profileCommentReqDto.getRecipientId());
        if (author.getId().equals(recipient.getId())) {
            throw new ProfileCommentException("You cannot create a comment for yourself");
        }
        ProfileComment profileComment = new ProfileComment();
        profileComment.setAuthor(author);
        profileComment.setRecipient(recipient);
        profileComment.setText(profileCommentReqDto.getText());
        profileCommentRepo.save(profileComment);
        return true;
    }
}
