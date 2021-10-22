package com.foretell.sportsmeetings.service.impl;

import com.foretell.sportsmeetings.dto.req.ProfileCommentReqDto;
import com.foretell.sportsmeetings.dto.res.ProfileCommentPageResDto;
import com.foretell.sportsmeetings.dto.res.ProfileCommentResDto;
import com.foretell.sportsmeetings.exception.ProfileCommentException;
import com.foretell.sportsmeetings.exception.ProfileCommentNotFoundException;
import com.foretell.sportsmeetings.model.ProfileComment;
import com.foretell.sportsmeetings.model.User;
import com.foretell.sportsmeetings.repo.ProfileCommentRepo;
import com.foretell.sportsmeetings.service.ProfileCommentService;
import com.foretell.sportsmeetings.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProfileCommentServiceImpl implements ProfileCommentService {

    private final UserService userService;
    private final ProfileCommentRepo profileCommentRepo;

    public ProfileCommentServiceImpl(UserService userService, ProfileCommentRepo profileCommentRepo) {
        this.userService = userService;
        this.profileCommentRepo = profileCommentRepo;
    }

    @Override
    public ProfileCommentResDto create(ProfileCommentReqDto profileCommentReqDto, String username) {
        User author = userService.findByUsername(username);
        User recipient = userService.findById(profileCommentReqDto.getRecipientId());
        if (author.getId().equals(recipient.getId())) {
            throw new ProfileCommentException("You cannot create a comment for yourself");
        }
        ProfileComment profileComment = new ProfileComment();
        profileComment.setAuthor(author);
        profileComment.setRecipient(recipient);
        profileComment.setText(profileCommentReqDto.getText());
       return convertProfileCommentToProfileCommentResDto(profileCommentRepo.save(profileComment));
    }

    @Override
    public ProfileCommentPageResDto getAllByRecipientId(Pageable pageable, Long recipientId) {
        Page<ProfileComment> page = profileCommentRepo.findAllByRecipientId(pageable, recipientId);
        List<ProfileCommentResDto> profileCommentResDtoList =
                page.getContent().stream().
                        map(this::convertProfileCommentToProfileCommentResDto).
                        collect(Collectors.toList());

        return new ProfileCommentPageResDto(
                profileCommentResDtoList,
                pageable.getPageNumber(),
                page.getTotalPages()
        );
    }

    @Override
    public ProfileCommentPageResDto getAllByUsername(Pageable pageable, String username) {
        User user = userService.findByUsername(username);
        Page<ProfileComment> page = profileCommentRepo.findAllByRecipientId(pageable, user.getId());
        List<ProfileCommentResDto> profileCommentResDtoList =
                page.getContent().stream().
                        map(this::convertProfileCommentToProfileCommentResDto).
                        collect(Collectors.toList());

        return new ProfileCommentPageResDto(
                profileCommentResDtoList,
                pageable.getPageNumber(),
                page.getTotalPages()
        );
    }

    @Override
    public ProfileComment findById(Long id) {
        return profileCommentRepo.findById(id).orElseThrow(() ->
                new ProfileCommentNotFoundException("ProfileComment with id: " + (id) + " not found"));
    }

    @Override
    public boolean deleteCommentByIdAndAuthorUsername(Long id, String username) {
        User user = userService.findByUsername(username);
        ProfileComment comment = findById(id);
        if (user.getId().equals(comment.getAuthor().getId())) {
            profileCommentRepo.delete(comment);
            return true;
        } else {
            throw new ProfileCommentException("You are not the author of the comment");
        }
    }

    @Override
    public boolean deleteById(Long id) {
        profileCommentRepo.deleteById(id);
        return true;
    }

    private ProfileCommentResDto convertProfileCommentToProfileCommentResDto(ProfileComment profileComment) {
        return new ProfileCommentResDto(
                profileComment.getId(),
                profileComment.getAuthor().getId(),
                profileComment.getText());
    }
}
