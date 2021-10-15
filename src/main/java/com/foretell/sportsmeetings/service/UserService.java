package com.foretell.sportsmeetings.service;

import com.foretell.sportsmeetings.dto.req.RegistrationReqDto;
import com.foretell.sportsmeetings.dto.res.UserInfoResDto;
import com.foretell.sportsmeetings.exception.InvalidProfilePhotoException;
import com.foretell.sportsmeetings.model.User;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    User register(RegistrationReqDto user);

    User findByUsername(String username);

    UserInfoResDto getUserInfo(String username);

    boolean loadProfilePhoto(MultipartFile photo, String username) throws InvalidProfilePhotoException;
}
