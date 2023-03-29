package gbrstd.clinica.service;

import gbrstd.clinica.dto.SignUpDto;
import gbrstd.clinica.exceptions.UserNotFoundException;
import gbrstd.clinica.exceptions.UsernameAlreadyExistException;
import gbrstd.clinica.model.User;
import gbrstd.clinica.repository.UserRepository;
import gbrstd.clinica.exceptions.PasswordMatchException;
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
