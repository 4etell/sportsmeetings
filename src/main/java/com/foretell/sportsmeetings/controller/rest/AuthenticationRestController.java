package com.foretell.sportsmeetings.controller.rest;

import com.foretell.sportsmeetings.dto.req.AuthenticationReqDto;
import com.foretell.sportsmeetings.dto.req.RegistrationReqDto;
import com.foretell.sportsmeetings.dto.res.AuthenticationResDto;
import com.foretell.sportsmeetings.service.UserService;
import com.foretell.sportsmeetings.security.jwt.JwtProvider;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

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

    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "400", description = "You sent invalid fields in your request"),
                    @ApiResponse(responseCode = "403", description = "Bad Credentials"),
                    @ApiResponse(responseCode = "404", description = "Username not found"),
                    @ApiResponse(responseCode = "409", description = "Username already exists")
            }
    )
    @PostMapping("login")
    public ResponseEntity<AuthenticationResDto> login(
            @RequestBody @Valid AuthenticationReqDto authenticationReqDto) {

        String username = authenticationReqDto.getUsername();

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,
                authenticationReqDto.getPassword()));

        String token = jwtProvider.generateToken(username);
        AuthenticationResDto authenticationResDto = new AuthenticationResDto(token);

        return ResponseEntity.ok(authenticationResDto);
    }


    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "400", description = "You sent invalid fields in your request"),
                    @ApiResponse(responseCode = "409", description = "Username already exists")
            }
    )
    @PostMapping("register")
    public ResponseEntity<AuthenticationResDto> register(
            @RequestBody @Valid RegistrationReqDto registrationReqDto) {

        String registeredUsername = userService.register(registrationReqDto).getUsername();
        String token = jwtProvider.generateToken(registeredUsername);
        AuthenticationResDto authenticationResDto = new AuthenticationResDto(token);

        return ResponseEntity.ok(authenticationResDto);
    }
}
