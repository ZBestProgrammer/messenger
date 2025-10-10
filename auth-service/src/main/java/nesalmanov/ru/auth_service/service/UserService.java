package nesalmanov.ru.auth_service.service;


import nesalmanov.ru.auth_service.jwt.JwtUtils;
import nesalmanov.ru.auth_service.mapper.UserMapper;
import nesalmanov.ru.auth_service.model.dto.request.GetUserRequest;
import nesalmanov.ru.auth_service.model.dto.request.GetUsersRequest;
import nesalmanov.ru.auth_service.model.dto.request.UserLoginRequest;
import nesalmanov.ru.auth_service.model.dto.request.UserRegisterRequest;
import nesalmanov.ru.auth_service.model.dto.response.UserResponse;
import nesalmanov.ru.auth_service.model.entity.User;
import nesalmanov.ru.auth_service.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final JwtUtils jwtUtils;

    private final UserMapper userMapper;

    public UserService(AuthenticationManager authenticationManager, UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, JwtUtils jwtUtils, UserMapper userMapper) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtUtils = jwtUtils;
        this.userMapper = userMapper;
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

        if (userRepository.findUserByUsername(userRegisterRequest.getUsername()) == null) {
            User newUser = new User();
            newUser.setUsername(userRegisterRequest.getUsername());
            newUser.setPassword(bCryptPasswordEncoder.encode(userRegisterRequest.getPassword()));
            newUser.setAvatar(userRegisterRequest.getAvatar());

            userRepository.save(newUser);

            return "Success";
        } else {
            return "User with this username already exist";
        }
    }

    public List<UserResponse> getUsers(GetUsersRequest getUserRequest) {
        List<User> users = userRepository.findByUsernameStartingWith(getUserRequest.getUsername());

        return userMapper.usersToUserResponses(users);
    }

    public UserResponse getUser(GetUserRequest getUserRequest) {
        return userMapper.userToUserResponse(userRepository.findUserById(getUserRequest.getId()));
    }

}
