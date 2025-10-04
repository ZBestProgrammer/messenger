package nesalmanov.ru.auth_service.service;


import nesalmanov.ru.auth_service.jwt.JwtUtils;
import nesalmanov.ru.auth_service.model.dto.request.UserLoginRequest;
import nesalmanov.ru.auth_service.model.dto.request.UserRegisterRequest;
import nesalmanov.ru.auth_service.model.entity.User;
import nesalmanov.ru.auth_service.repository.UserRepository;
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

    public String login(UserLoginRequest userLoginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userLoginRequest.getUsername(), userLoginRequest.getPassword())
        );

        if (authentication.isAuthenticated()) {
            User user = userRepository.findUserByUsername(userLoginRequest.getUsername());
            return jwtUtils.generateMobileToken(userLoginRequest, user);
        }
        return "Bad credentials";
    }

    public String register(UserRegisterRequest userRegisterRequest) {
        User newUser = new User();
        newUser.setUsername(userRegisterRequest.getUsername());
        newUser.setPassword(bCryptPasswordEncoder.encode(userRegisterRequest.getPassword()));
        newUser.setAvatar(userRegisterRequest.getAvatar());

        try {
            userRepository.save(newUser);
            return "Success";
        } catch (Exception e) {
            return "Error";
        }
    }

}
