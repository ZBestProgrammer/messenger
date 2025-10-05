package nesalmanov.ru.auth_service.model.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetUserRequest {
    private String username;
}
