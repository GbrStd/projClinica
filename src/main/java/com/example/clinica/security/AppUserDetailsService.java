package com.example.clinica.security;

import com.example.clinica.exceptions.UserNotFoundException;
import com.example.clinica.model.User;
import com.example.clinica.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AppUserDetailsService implements UserDetailsService {

    private static final String USER_NOT_FOUND_MSG = "Usuário ou senha inválidos";
    private final UserService userService;

    public AppUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user;
        try {
            user = userService.findByUsername(username);
        } catch (UserNotFoundException e) {
            throw new UsernameNotFoundException(USER_NOT_FOUND_MSG);
        }
        return new AppUserDetails(user);
    }

}
