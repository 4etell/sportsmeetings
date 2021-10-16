package com.foretell.sportsmeetings.dto.res;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class UserInfoResDto {
    private final Long id;
    private final String username;
    private final String firstName;
    private final String lastName;
    private final List<String> roles;
    private final String email;
}
