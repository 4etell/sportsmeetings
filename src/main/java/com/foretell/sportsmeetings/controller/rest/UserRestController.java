package com.foretell.sportsmeetings.controller.rest;

import com.foretell.sportsmeetings.dto.res.UserInfoResDto;
import com.foretell.sportsmeetings.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserRestController {

    private final UserService userService;

    public UserRestController( UserService userService) {
        this.userService = userService;
    }

    @GetMapping("user/info/{userId}")
    public UserInfoResDto getUserInfo(@PathVariable Long userId) {
        return userService.getUserInfoById(userId);
    }
}
