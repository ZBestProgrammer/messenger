package nesalmanov.ru.authservice.controller;

import nesalmanov.ru.authservice.model.dto.request.UserLoginRequest;
import nesalmanov.ru.authservice.model.dto.request.UserRegisterRequest;
import nesalmanov.ru.authservice.service.UserService;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

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

}

