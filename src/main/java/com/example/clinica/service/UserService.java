package com.example.clinica.service;

import com.example.clinica.dto.SignUpDto;
import com.example.clinica.exceptions.PasswordMatchException;
import com.example.clinica.exceptions.UserNotFoundException;
import com.example.clinica.exceptions.UsernameAlreadyExistException;
import com.example.clinica.model.User;
import com.example.clinica.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User findByUsername(String username) throws UserNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public void signUp(SignUpDto signUpDto) throws UsernameAlreadyExistException, PasswordMatchException {

        if (signUpDto.getPassword() != null && !signUpDto.getPassword().equals(signUpDto.getConfirmPassword()))
            throw new PasswordMatchException("As senhas não conferem");

        if (existsByUsername(signUpDto.getUsername()))
            throw new UsernameAlreadyExistException(String.format("Usuário %s já cadastrado", signUpDto.getUsername()));

        User user = new User();

        user.setUsername(signUpDto.getUsername());
        user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));

        userRepository.save(user);
    }

}
