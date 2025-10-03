package nesalmanov.ru.auth_service.model.dto.request;

import lombok.Data;

@Data
public class UserRegisterRequest {

    private String username;
    private String password;
    private String avatar;

}
