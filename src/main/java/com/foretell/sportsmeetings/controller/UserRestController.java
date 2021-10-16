package com.foretell.sportsmeetings.controller;

import com.foretell.sportsmeetings.dto.req.ChangeProfileReqDto;
import com.foretell.sportsmeetings.dto.res.UserInfoResDto;
import com.foretell.sportsmeetings.exception.InvalidProfilePhotoException;
import com.foretell.sportsmeetings.service.UserService;
import com.foretell.sportsmeetings.util.jwt.JwtProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RestController
public class UserRestController {

    private final JwtProvider jwtProvider;
    private final UserService userService;

    public UserRestController(JwtProvider jwtProvider, UserService userService) {
        this.jwtProvider = jwtProvider;
        this.userService = userService;
    }

    @GetMapping("me")
    public UserInfoResDto getUserInfo(HttpServletRequest httpServletRequest) {
        String usernameFromToken =
                jwtProvider.getUsernameFromToken(jwtProvider.getTokenFromRequest(httpServletRequest));
        return userService.getUserInfoByUsername(usernameFromToken);
    }

    @GetMapping("user/info/{userId}")
    public UserInfoResDto getUserInfo(@PathVariable Long userId) {
        return userService.getUserInfoById(userId);
    }

    @PostMapping("load-profile-photo")
    public ResponseEntity<?> loadProfilePhoto(@RequestPart MultipartFile photo, HttpServletRequest httpServletRequest) throws InvalidProfilePhotoException, MaxUploadSizeExceededException {
        String usernameFromToken =
                jwtProvider.getUsernameFromToken(jwtProvider.getTokenFromRequest(httpServletRequest));
        if (userService.loadProfilePhoto(photo, usernameFromToken)) {
            return ResponseEntity.ok("Photo loaded successfully");
        } else {
            return ResponseEntity.status(500).body("Something wrong on server");
        }
    }

    @PostMapping("change-profile")
    public ResponseEntity<?> changeProfile(@RequestBody ChangeProfileReqDto changeProfileReqDto,
                                           HttpServletRequest httpServletRequest) {
        String usernameFromToken =
                jwtProvider.getUsernameFromToken(jwtProvider.getTokenFromRequest(httpServletRequest));
        if (userService.changeProfile(changeProfileReqDto, usernameFromToken)) {
            return ResponseEntity.ok("Profile info changed successfully");
        } else {
            return ResponseEntity.status(500).body("Something wrong on server");
        }
    }
}
