package com.foretell.sportsmeetings.service;

import com.foretell.sportsmeetings.dto.req.ProfileInfoReqDto;
import com.foretell.sportsmeetings.dto.req.RegistrationReqDto;
import com.foretell.sportsmeetings.dto.res.TelegramBotActivationCodeResDto;
import com.foretell.sportsmeetings.dto.res.UserInfoResDto;
import com.foretell.sportsmeetings.exception.InvalidProfilePhotoException;
import com.foretell.sportsmeetings.model.Role;
import com.foretell.sportsmeetings.model.User;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    User register(RegistrationReqDto user);

    User findByUsername(String username);

    User findById(Long id);

    UserInfoResDto getUserInfoByUsername(String username);

    UserInfoResDto getUserInfoById(Long id);

    UserInfoResDto changeProfile(ProfileInfoReqDto profileInfoReqDto, String username);

    TelegramBotActivationCodeResDto getTelegramBotActivationCode(String username);

    boolean setUserRole(Long userId, String roleName);

    boolean activateTelegramBot(String telegramBotActivationCode, Long telegramBotChatId);

    boolean loadProfilePhoto(MultipartFile photo, String username) throws InvalidProfilePhotoException;
}
