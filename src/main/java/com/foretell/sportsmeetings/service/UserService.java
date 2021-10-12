package com.foretell.sportsmeetings.service;

import com.foretell.sportsmeetings.dto.req.RegistrationReqDto;
import com.foretell.sportsmeetings.model.User;

import java.util.List;

public interface UserService {
    User register(RegistrationReqDto user);

    User findByUsername(String username);

    List<String> findUserRolesByUsername(String username);
}
