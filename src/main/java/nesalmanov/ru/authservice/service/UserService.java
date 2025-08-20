package nesalmanov.ru.authservice.service;

import nesalmanov.ru.authservice.model.dto.request.UserLoginRequest;
import nesalmanov.ru.authservice.model.dto.request.UserRegisterRequest;
import nesalmanov.ru.authservice.model.entity.User;
import nesalmanov.ru.authservice.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(AuthenticationManager authenticationManager, UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public String login(UserLoginRequest userLoginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userLoginRequest.getUsername(), userLoginRequest.getPassword())
        );

        if (authentication.isAuthenticated()) {
            return "Hello";
        }
        return "Bad credentials";
    }

    public String register(UserRegisterRequest userRegisterRequest) {
        User newUser = new User();
        newUser.setUsername(userRegisterRequest.getUsername());
        newUser.setPassword(bCryptPasswordEncoder.encode(userRegisterRequest.getPassword()));

        try {
            userRepository.save(newUser);
            return "Success";
        } catch (Exception e) {
            return "Error";
        }
    }

}
