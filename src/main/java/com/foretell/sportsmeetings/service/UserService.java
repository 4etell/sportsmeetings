package com.foretell.sportsmeetings.service;

import com.foretell.sportsmeetings.dto.req.RegistrationReqDto;
import com.foretell.sportsmeetings.dto.res.UserInfoResDto;
import com.foretell.sportsmeetings.model.User;

public interface UserService {
    User register(RegistrationReqDto user);

    User findByUsername(String username);

    UserInfoResDto getUserInfo(String username);
}
