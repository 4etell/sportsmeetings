package com.foretell.sportsmeetings.controller.rest;

import com.foretell.sportsmeetings.dto.req.ProfileInfoReqDto;
import com.foretell.sportsmeetings.dto.res.UserInfoResDto;
import com.foretell.sportsmeetings.exception.InvalidProfilePhotoException;
import com.foretell.sportsmeetings.service.UserService;
import com.foretell.sportsmeetings.util.jwt.JwtProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("my-profile")
public class MyProfileRestController {

    private final JwtProvider jwtProvider;
    private final UserService userService;

    public MyProfileRestController(JwtProvider jwtProvider, UserService userService) {
        this.jwtProvider = jwtProvider;
        this.userService = userService;
    }

    @GetMapping("info")
    public UserInfoResDto getInfo(HttpServletRequest httpServletRequest) {
        String usernameFromToken =
                jwtProvider.getUsernameFromToken(jwtProvider.getTokenFromRequest(httpServletRequest));
        return userService.getUserInfoByUsername(usernameFromToken);
    }

    @PutMapping("info")
    public UserInfoResDto changeInfo(@RequestBody @Valid ProfileInfoReqDto profileInfoReqDto,
                                     HttpServletRequest httpServletRequest) {
        String usernameFromToken =
                jwtProvider.getUsernameFromToken(jwtProvider.getTokenFromRequest(httpServletRequest));

        return userService.changeProfile(profileInfoReqDto, usernameFromToken);
    }

    @PutMapping("photo")
    public ResponseEntity<?> loadPhoto(@RequestPart MultipartFile photo,
                                       HttpServletRequest httpServletRequest) throws InvalidProfilePhotoException, MaxUploadSizeExceededException {
        String usernameFromToken =
                jwtProvider.getUsernameFromToken(jwtProvider.getTokenFromRequest(httpServletRequest));
        if (userService.loadProfilePhoto(photo, usernameFromToken)) {
            return ResponseEntity.ok("Photo loaded successfully");
        } else {
            return ResponseEntity.status(500).body("Something wrong on server");
        }
    }
}
