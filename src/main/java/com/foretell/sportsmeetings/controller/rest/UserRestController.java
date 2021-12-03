package com.foretell.sportsmeetings.controller.rest;

import com.foretell.sportsmeetings.dto.req.ProfileCommentReqDto;
import com.foretell.sportsmeetings.dto.req.ProfileInfoReqDto;
import com.foretell.sportsmeetings.dto.res.ProfileCommentResDto;
import com.foretell.sportsmeetings.dto.res.UserInfoResDto;
import com.foretell.sportsmeetings.dto.res.page.extnds.PageProfileCommentResDto;
import com.foretell.sportsmeetings.exception.InvalidProfilePhotoException;
import com.foretell.sportsmeetings.service.ProfileCommentService;
import com.foretell.sportsmeetings.service.UserService;
import com.foretell.sportsmeetings.security.jwt.JwtProvider;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("users")
public class UserRestController {

    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;
    private final ProfileCommentService profileCommentService;

    public UserRestController(UserService userService, JwtProvider jwtProvider, AuthenticationManager authenticationManager, ProfileCommentService profileCommentService) {
        this.userService = userService;
        this.jwtProvider = jwtProvider;
        this.authenticationManager = authenticationManager;
        this.profileCommentService = profileCommentService;
    }

    @RequestMapping(value = "{userId}", method = RequestMethod.GET)
    public UserInfoResDto getUserInfo(@PathVariable Long userId) {
        return userService.getUserInfoById(userId);
    }

    @RequestMapping(value = "info", method = RequestMethod.GET)
    public UserInfoResDto getMyInfo(HttpServletRequest httpServletRequest) {
        String usernameFromToken =
                jwtProvider.getUsernameFromToken(jwtProvider.getTokenFromRequest(httpServletRequest));
        return userService.getUserInfoByUsername(usernameFromToken);
    }

    @RequestMapping(value = "info", method = RequestMethod.PUT)
    public UserInfoResDto changeMyInfo(@RequestBody @Valid ProfileInfoReqDto profileInfoReqDto,
                                       HttpServletRequest httpServletRequest) {
        String usernameFromToken =
                jwtProvider.getUsernameFromToken(jwtProvider.getTokenFromRequest(httpServletRequest));

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(usernameFromToken, profileInfoReqDto.getConfirmPassword()));

        return userService.changeProfile(profileInfoReqDto, usernameFromToken);
    }

    @RequestMapping(value = "photo", method = RequestMethod.PUT)
    public ResponseEntity<?> loadMyPhoto(@RequestPart MultipartFile photo,
                                         HttpServletRequest httpServletRequest) throws InvalidProfilePhotoException, MaxUploadSizeExceededException {
        String usernameFromToken =
                jwtProvider.getUsernameFromToken(jwtProvider.getTokenFromRequest(httpServletRequest));
        if (userService.loadProfilePhoto(photo, usernameFromToken)) {
            return ResponseEntity.ok("Photo loaded successfully");
        } else {
            return ResponseEntity.status(500).body("Something wrong on server");
        }
    }

    @RequestMapping(value = "{userId}/comments", method = RequestMethod.POST)
    public ProfileCommentResDto createComment(@PathVariable Long userId,
                                              @RequestBody @Valid ProfileCommentReqDto profileCommentReqDto,
                                              HttpServletRequest httpServletRequest) {
        String usernameFromToken =
                jwtProvider.getUsernameFromToken(jwtProvider.getTokenFromRequest(httpServletRequest));

        return profileCommentService.create(userId, profileCommentReqDto, usernameFromToken);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", paramType = "query",
                    value = "Results page you want to retrieve (0..N)"),
    })
    @RequestMapping(value = "{userId}/comments", method = RequestMethod.GET)
    public PageProfileCommentResDto getCommentsByRecipientId(
            @PathVariable Long userId,
            @PageableDefault(size = 3, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return profileCommentService.getAllByRecipientId(pageable, userId);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", paramType = "query",
                    value = "Results page you want to retrieve (0..N)"),
    })
    @RequestMapping(value = "comments", method = RequestMethod.GET)
    public PageProfileCommentResDto getMyCreatedComments(
            HttpServletRequest httpServletRequest,
            @PageableDefault(size = 3, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable) {

        String usernameFromToken = jwtProvider.getUsernameFromToken(
                jwtProvider.getTokenFromRequest(httpServletRequest));
        return profileCommentService.getAllByUsername(pageable, usernameFromToken);
    }

    @RequestMapping(value = "comments/{commentId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteMyCreatedComment(@PathVariable Long commentId,
                                                    HttpServletRequest httpServletRequest) {
        String usernameFromToken = jwtProvider.getUsernameFromToken(
                jwtProvider.getTokenFromRequest(httpServletRequest));

        if (profileCommentService.deleteCommentByIdAndAuthorUsername(commentId, usernameFromToken)) {
            return ResponseEntity.ok("Comment successfully deleted");
        } else {
            return ResponseEntity.internalServerError().body("Something wrong on server");
        }
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @RequestMapping(value = "{userId}/ban", method = RequestMethod.POST)
    public ResponseEntity<?> banUser(@PathVariable Long userId) {
        if (userService.setUserRole(userId, "ROLE_BANNED")) {
            return ResponseEntity.ok("Successfully banned");
        } else {
            return ResponseEntity.internalServerError().body("Something wrong on server");
        }
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @RequestMapping(value = "{userId}/unban", method = RequestMethod.POST)
    public ResponseEntity<?> unbanUser(@PathVariable Long userId) {
        if (userService.setUserRole(userId, "ROLE_USER")) {
            return ResponseEntity.ok("Successfully unbanned");
        } else {
            return ResponseEntity.internalServerError().body("Something wrong on server");
        }
    }
}
