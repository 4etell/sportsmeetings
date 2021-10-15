package com.foretell.sportsmeetings.controller;

import com.foretell.sportsmeetings.dto.res.UserInfoResDto;
import com.foretell.sportsmeetings.exception.InvalidProfilePhotoException;
import com.foretell.sportsmeetings.service.UserService;
import com.foretell.sportsmeetings.util.jwt.JwtProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("user")
public class UserRestController {

    private final JwtProvider jwtProvider;
    private final UserService userService;

    public UserRestController(JwtProvider jwtProvider, UserService userService) {
        this.jwtProvider = jwtProvider;
        this.userService = userService;
    }

    @GetMapping("info")
    public UserInfoResDto getUserInfo(HttpServletRequest httpServletRequest) {
        String usernameFromToken =
                jwtProvider.getUsernameFromToken(jwtProvider.getTokenFromRequest(httpServletRequest));
        return userService.getUserInfo(usernameFromToken);
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
}
