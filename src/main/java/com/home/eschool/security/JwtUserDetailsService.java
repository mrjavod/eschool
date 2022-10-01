package com.home.eschool.security;

import com.home.eschool.entity.Users;
import com.home.eschool.security.jwt.JwtUserFactory;
import com.home.eschool.services.UserService;
import com.home.eschool.utils.Settings;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    private final UserService userService;

    public JwtUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {

        Users user = userService.findUserByLogin(login);

        if (user == null) {
            throw new UsernameNotFoundException("User with username: " + login + " not found");
        }

        Settings.setCurrentUser(user);
        return JwtUserFactory.create(user);
    }
}
