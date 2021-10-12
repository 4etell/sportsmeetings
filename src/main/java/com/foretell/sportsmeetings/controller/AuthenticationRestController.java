package com.foretell.sportsmeetings.controller;

import com.foretell.sportsmeetings.dto.req.AuthenticationReqDto;
import com.foretell.sportsmeetings.dto.req.RegistrationReqDto;
import com.foretell.sportsmeetings.dto.res.AuthenticationResDto;
import com.foretell.sportsmeetings.service.UserService;
import com.foretell.sportsmeetings.util.jwt.JwtProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/auth")
public class AuthenticationRestController {

    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;

    public AuthenticationRestController(UserService userService, JwtProvider jwtProvider,
                                        AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtProvider = jwtProvider;
        this.authenticationManager = authenticationManager;
    }


    @PostMapping("login")
    public ResponseEntity<AuthenticationResDto> login(
            @RequestBody @Valid AuthenticationReqDto authenticationReqDto) {

        String username = authenticationReqDto.getUsername();

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,
                authenticationReqDto.getPassword()));

        String token = jwtProvider.generateToken(username);
        List<String> userRolesByUsername = userService.findUserRolesByUsername(username);
        AuthenticationResDto authenticationResDto = new AuthenticationResDto(username, token, userRolesByUsername);

        return ResponseEntity.ok(authenticationResDto);
    }


    @PostMapping("register")
    public ResponseEntity<AuthenticationResDto> register(
            @RequestBody @Valid RegistrationReqDto registrationReqDto) {

        String registeredUsername = userService.register(registrationReqDto).getUsername();
        String token = jwtProvider.generateToken(registeredUsername);
        List<String> userRolesByUsername = userService.findUserRolesByUsername(registeredUsername);
        AuthenticationResDto authenticationResDto =
                new AuthenticationResDto(registeredUsername, token, userRolesByUsername);

        return ResponseEntity.ok(authenticationResDto);
    }
}
