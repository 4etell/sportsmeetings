package com.foretell.sportsmeetings.service.impl;

import com.foretell.sportsmeetings.dto.req.ChangeProfileReqDto;
import com.foretell.sportsmeetings.dto.req.RegistrationReqDto;
import com.foretell.sportsmeetings.dto.res.UserInfoResDto;
import com.foretell.sportsmeetings.exception.InvalidProfilePhotoException;
import com.foretell.sportsmeetings.exception.RoleNotFoundException;
import com.foretell.sportsmeetings.exception.UserNotFoundException;
import com.foretell.sportsmeetings.exception.UsernameAlreadyExistsException;
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
            log.info("IN register - user: {} successfully registered", registeredUser);

            return registeredUser;
        } else {
            log.error("User with username: " + usernameFromDto + " already exists");
            throw new UsernameAlreadyExistsException("User with username: " +
                    usernameFromDto + " already exists");

        }
    }

    @Override
    public User findByUsername(String username) {
        return userRepo.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Override
    public User findById(Long id) {
        return userRepo.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
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
    public boolean changeProfile(ChangeProfileReqDto changeProfileReqDto, String username) {
        User user = findByUsername(username);
        user.setEmail(changeProfileReqDto.getEmail());
        user.setFirstName(changeProfileReqDto.getFirstName());
        user.setLastName(changeProfileReqDto.getLastName());
        user.setPassword(passwordEncoder.encode(changeProfileReqDto.getPassword()));
        userRepo.save(user);
        return true;
    }

    @Override
    public boolean loadProfilePhoto(MultipartFile photo, String username) throws InvalidProfilePhotoException {
        if (photo.isEmpty()) {
            throw new InvalidProfilePhotoException("Photo is empty");
        }
        User user = findByUsername(username);
        String suffix = photo.getOriginalFilename().substring(photo.getOriginalFilename().lastIndexOf(".") + 1);
        if (suffix.equalsIgnoreCase("jpg") ||
                suffix.equalsIgnoreCase("jpeg") ||
                suffix.equalsIgnoreCase("png")) {
            String fileName = (user.getUsername()) + ".jpg";
            createProfilePhotoDirIfNotExists();
            String filePath = profilePhotoPath + fileName;
            try {
                photo.transferTo(new File(filePath));
            } catch (IOException e) {
                throw new InvalidProfilePhotoException(e.getMessage());
            }
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
                null);
    }

    private UserInfoResDto convertUserToUserInfoResDto(User user) {
        return new UserInfoResDto(
                user.getId(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                getRoleNames(user.getRoles())
        );
    }

    private void createProfilePhotoDirIfNotExists() {
        File loadDir = new File(profilePhotoPath);

        if (!loadDir.exists()) {
            loadDir.mkdir();
        }
    }
}