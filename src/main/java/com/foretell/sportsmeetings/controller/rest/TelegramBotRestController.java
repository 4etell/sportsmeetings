package com.foretell.sportsmeetings.controller.rest;

import com.foretell.sportsmeetings.dto.res.TelegramBotActivationCodeResDto;
import com.foretell.sportsmeetings.service.UserService;
import com.foretell.sportsmeetings.security.jwt.JwtProvider;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("telegram-bot")
public class TelegramBotRestController {

    private final JwtProvider jwtProvider;
    private final UserService userService;

    public TelegramBotRestController(JwtProvider jwtProvider, UserService userService) {
        this.jwtProvider = jwtProvider;
        this.userService = userService;
    }

    @GetMapping("activation-code")
    public TelegramBotActivationCodeResDto getActivationCode(HttpServletRequest httpServletRequest) {
        String usernameFromToken =
                jwtProvider.getUsernameFromToken(jwtProvider.getTokenFromRequest(httpServletRequest));

        return userService.getTelegramBotActivationCode(usernameFromToken);
    }
}
