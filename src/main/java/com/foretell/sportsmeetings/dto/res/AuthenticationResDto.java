package com.foretell.sportsmeetings.dto.res;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AuthenticationResDto {

    private String username;

    private String token;

    private List<String> roles;

}