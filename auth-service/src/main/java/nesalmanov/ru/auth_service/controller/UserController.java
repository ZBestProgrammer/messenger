package nesalmanov.ru.auth_service.controller;


import nesalmanov.ru.auth_service.model.dto.request.GetUserRequest;
import nesalmanov.ru.auth_service.model.dto.request.UserLoginRequest;
import nesalmanov.ru.auth_service.model.dto.request.UserRegisterRequest;
import nesalmanov.ru.auth_service.model.dto.response.UserResponse;
import nesalmanov.ru.auth_service.service.UserService;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/csrf")
    public String getCsrfToken(CsrfToken csrfToken) {
        return csrfToken.getToken();
    }

    @PostMapping("/login")
    public String login(@RequestBody UserLoginRequest userLoginRequest) {
        return userService.login(userLoginRequest);
    }

    @PostMapping("/register")
    public String register(@RequestBody UserRegisterRequest userRegisterRequest) {
        return userService.register(userRegisterRequest);
    }

    @PostMapping("/getUsers")
    public List<UserResponse> getUsers(@RequestBody GetUserRequest getUserRequest) {
        return userService.getUsers(getUserRequest);
    }

}

