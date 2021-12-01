package com.foretell.sportsmeetings.security.jwt;

import com.foretell.sportsmeetings.model.User;
import com.foretell.sportsmeetings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Autowired
    public JwtUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public JwtUser loadUserByUsername(String username) {
        User user = userService.findByUsername(username);
        return JwtUser.fromUserToJwtUser(user);
    }
}
