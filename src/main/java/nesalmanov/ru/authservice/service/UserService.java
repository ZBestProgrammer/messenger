package nesalmanov.ru.authservice.service;

import jakarta.servlet.http.HttpServletResponse;
import nesalmanov.ru.authservice.jwt.JwtUtils;
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

    private final JwtUtils jwtUtils;

    public UserService(AuthenticationManager authenticationManager, UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtUtils = jwtUtils;
    }

    public String login(UserLoginRequest userLoginRequest, HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userLoginRequest.getUsername(), userLoginRequest.getPassword())
        );

        if (authentication.isAuthenticated()) {
            return jwtUtils.generateToken(userLoginRequest, response);
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
