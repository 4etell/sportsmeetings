package com.foretell.sportsmeetings.service.impl;

import com.foretell.sportsmeetings.dto.req.RegistrationReqDto;
import com.foretell.sportsmeetings.dto.res.UserInfoResDto;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final BCryptPasswordEncoder passwordEncoder;

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
    public UserInfoResDto getUserInfo(String username) {
        return convertUserToUserInfoResDto(findByUsername(username));
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
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                getRoleNames(user.getRoles())
        );
    }
}