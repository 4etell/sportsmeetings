package com.foretell.sportsmeetings.controller;

import com.foretell.sportsmeetings.dto.req.ProfileCommentReqDto;
import com.foretell.sportsmeetings.service.ProfileCommentService;
import com.foretell.sportsmeetings.util.jwt.JwtProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("comment")
public class ProfileCommentRestController {

    private final JwtProvider jwtProvider;
    private final ProfileCommentService profileCommentService;

    public ProfileCommentRestController(JwtProvider jwtProvider, ProfileCommentService profileCommentService) {
        this.jwtProvider = jwtProvider;
        this.profileCommentService = profileCommentService;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid ProfileCommentReqDto profileCommentReqDto,
                                    HttpServletRequest httpServletRequest) {
        if (profileCommentService.create(profileCommentReqDto,
                jwtProvider.getUsernameFromToken(jwtProvider.getTokenFromRequest(httpServletRequest)))) {
            return ResponseEntity.ok().body("Comment successfully created");
        } else {
            return ResponseEntity.internalServerError().body("Something wrong on server");
        }
    }
}
