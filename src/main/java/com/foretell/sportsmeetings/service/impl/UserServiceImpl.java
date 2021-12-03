package com.foretell.sportsmeetings.service.impl;

import com.foretell.sportsmeetings.dto.req.ProfileInfoReqDto;
import com.foretell.sportsmeetings.dto.req.RegistrationReqDto;
import com.foretell.sportsmeetings.dto.res.TelegramBotActivationCodeResDto;
import com.foretell.sportsmeetings.dto.res.UserInfoResDto;
import com.foretell.sportsmeetings.exception.InvalidProfilePhotoException;
import com.foretell.sportsmeetings.exception.UsernameAlreadyExistsException;
import com.foretell.sportsmeetings.exception.notfound.RoleNotFoundException;
import com.foretell.sportsmeetings.exception.notfound.UserNotFoundException;
import com.foretell.sportsmeetings.model.Role;
import com.foretell.sportsmeetings.model.User;
import com.foretell.sportsmeetings.repo.RoleRepo;
import com.foretell.sportsmeetings.repo.UserRepo;
import com.foretell.sportsmeetings.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final BCryptPasswordEncoder passwordEncoder;

    @Value("${profile.photo.path}")
    private String profilePhotoPath;

    @Autowired
    public UserServiceImpl(UserRepo userRepo, RoleRepo roleRepo, BCryptPasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User register(RegistrationReqDto registrationReqDto) {

        String usernameFromDto = registrationReqDto.getUsername();

        if (userRepo.findByUsername(usernameFromDto).isEmpty()) {

            User user = convertRegistrationReqDtoToUser(registrationReqDto);

            Role roleUser = roleRepo.findByName("ROLE_USER").
                    orElseThrow(() -> new RoleNotFoundException("ROLE_USER not found"));
            List<Role> userRoles = new ArrayList<>();
            userRoles.add(roleUser);

            user.setRoles(userRoles);

            User registeredUser = userRepo.save(user);
            log.info("IN register - user {} successfully registered", registeredUser.getUsername());

            return registeredUser;
        } else {
            log.error("User with username: " + usernameFromDto + " already exists");
            throw new UsernameAlreadyExistsException("User with username: " +
                    usernameFromDto + " already exists");

        }
    }

    @Override
    public User findByUsername(String username) {
        return userRepo.findByUsername(username).orElseThrow(() ->
                new UserNotFoundException("User with username: " + (username) + " not found"));
    }

    @Override
    public User findById(Long id) {
        return userRepo.findById(id).orElseThrow(() ->
                new UserNotFoundException("User with id: " + (id) + " not found"));
    }

    @Override
    public UserInfoResDto getUserInfoByUsername(String username) {
        return convertUserToUserInfoResDto(findByUsername(username));
    }

    @Override
    public UserInfoResDto getUserInfoById(Long id) {
        return convertUserToUserInfoResDto(findById(id));
    }

    @Override
    public UserInfoResDto changeProfile(ProfileInfoReqDto profileInfoReqDto, String username) {
        User user = findByUsername(username);
        user.setEmail(profileInfoReqDto.getEmail());
        user.setFirstName(profileInfoReqDto.getFirstName());
        user.setLastName(profileInfoReqDto.getLastName());
        if (profileInfoReqDto.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(profileInfoReqDto.getPassword()));
        }
        User updatedUser = userRepo.save(user);
        log.info("IN changeProfile - user: {} successfully updated info", updatedUser.getUsername());
        return convertUserToUserInfoResDto(updatedUser);
    }

    @Override
    public TelegramBotActivationCodeResDto getTelegramBotActivationCode(String username) {
        User user = findByUsername(username);
        return new TelegramBotActivationCodeResDto(user.getTelegramBotActivationCode());
    }

    @Override
    public boolean setUserRole(Long userId, String roleName) {
        User user = findById(userId);
        List<Role> roles = new ArrayList<Role>();
        Role roleUser = roleRepo.findByName(roleName).
                orElseThrow(() -> new RoleNotFoundException(roleName + " not found"));
        roles.add(roleUser);
        user.setRoles(roles);
        userRepo.save(user);
        return true;
    }

    @Override
    public boolean activateTelegramBot(String telegramBotActivationCode, Long telegramBotChatId) {
        User user = userRepo.findByTelegramBotActivationCode(telegramBotActivationCode).orElseThrow(
                () -> new UserNotFoundException("User with activation code " + (telegramBotActivationCode) + " not found"));
        user.setTelegramBotChatId(telegramBotChatId);
        userRepo.save(user);
        return true;
    }

    @Override
    public boolean loadProfilePhoto(MultipartFile photo, String username) throws InvalidProfilePhotoException {
        if (photo.isEmpty()) {
            throw new InvalidProfilePhotoException("Photo is empty");
        }
        User user = findByUsername(username);
        String suffix = Objects.requireNonNull(photo.getOriginalFilename()).substring(photo.getOriginalFilename().lastIndexOf(".") + 1);
        if (suffix.equalsIgnoreCase("jpg") ||
                suffix.equalsIgnoreCase("jpeg") ||
                suffix.equalsIgnoreCase("png")) {
            String fileName = (user.getUsername()) + ".jpg";
            createProfilePhotoDirIfNotExists();
            String filePath = profilePhotoPath + fileName;
            try {
                photo.transferTo(new File(filePath));
            } catch (IOException e) {
                log.error(e.getMessage());
                throw new InvalidProfilePhotoException(e.getMessage());
            }
            log.info("In loadProfilePhoto - username: " +
                    user.getUsername() + " successfully loaded profile photo");
            return true;
        } else {
            throw new InvalidProfilePhotoException("Photo format can only be of these types: .jpeg, .png, .jpg");
        }
    }

    private List<String> getRoleNames(List<Role> userRoles) {
        if (userRoles != null) {
            List<String> result = new ArrayList<>();

            userRoles.forEach(role -> {
                result.add(role.getName());
            });

            return result;
        } else {
            throw new RoleNotFoundException("User roles is null!!!");
        }
    }


    private User convertRegistrationReqDtoToUser(RegistrationReqDto registrationReqDto) {
        return new User(
                registrationReqDto.getUsername(),
                registrationReqDto.getFirstName(),
                registrationReqDto.getLastName(),
                registrationReqDto.getEmail(),
                passwordEncoder.encode(registrationReqDto.getPassword()),
                false,
                UUID.randomUUID().toString(),
                null,
                null,
                null,
                null);
    }

    private UserInfoResDto convertUserToUserInfoResDto(User user) {
        return new UserInfoResDto(
                user.getId(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                getRoleNames(user.getRoles()),
                user.getEmail()
        );
    }

    private void createProfilePhotoDirIfNotExists() {
        File loadDir = new File(profilePhotoPath);

        if (!loadDir.exists()) {
            loadDir.mkdir();
        }
    }
}