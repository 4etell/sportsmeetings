package com.foretell.sportsmeetings.controller;

import com.foretell.sportsmeetings.dto.req.ProfileCommentReqDto;
import com.foretell.sportsmeetings.dto.res.ProfileCommentPageResDto;
import com.foretell.sportsmeetings.service.ProfileCommentService;
import com.foretell.sportsmeetings.util.jwt.JwtProvider;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
public class ProfileCommentRestController {

    private final JwtProvider jwtProvider;
    private final ProfileCommentService profileCommentService;

    public ProfileCommentRestController(JwtProvider jwtProvider, ProfileCommentService profileCommentService) {
        this.jwtProvider = jwtProvider;
        this.profileCommentService = profileCommentService;
    }

    @RequestMapping(value = "/comments", method = RequestMethod.POST)
    public ResponseEntity<?> create(@RequestBody @Valid ProfileCommentReqDto profileCommentReqDto,
                                    HttpServletRequest httpServletRequest) {
        if (profileCommentService.create(profileCommentReqDto,
                jwtProvider.getUsernameFromToken(jwtProvider.getTokenFromRequest(httpServletRequest)))) {
            return ResponseEntity.ok().body("Comment successfully created");
        } else {
            return ResponseEntity.internalServerError().body("Something wrong on server");
        }
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
                    value = "Results page you want to retrieve (0..N)"),
    })
    @GetMapping("comments/{recipientId}")
    public ProfileCommentPageResDto getCommentsByRecipientId(
            @PathVariable Long recipientId,
            @PageableDefault(size = 3, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return profileCommentService.getAllByRecipientId(pageable, recipientId);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
                    value = "Results page you want to retrieve (0..N)"),
    })
    @GetMapping("my-profile-comments")
    public ProfileCommentPageResDto getMyComments(
            HttpServletRequest httpServletRequest,
            @PageableDefault(size = 3, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable) {

        String usernameFromToken = jwtProvider.getUsernameFromToken(
                jwtProvider.getTokenFromRequest(httpServletRequest));
        return profileCommentService.getAllByUsername(pageable, usernameFromToken);
    }

    @RequestMapping(value = "/my-comments/{commentId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteMyComment(@PathVariable Long commentId,
                                                         HttpServletRequest httpServletRequest) {
        String usernameFromToken = jwtProvider.getUsernameFromToken(
                jwtProvider.getTokenFromRequest(httpServletRequest));

        if (profileCommentService.deleteCommentByIdAndAuthorUsername(commentId, usernameFromToken)) {
            return ResponseEntity.ok("Comment successfully deleted");
        } else {
            return ResponseEntity.internalServerError().body("Something wrong on server");
        }
    }
}
