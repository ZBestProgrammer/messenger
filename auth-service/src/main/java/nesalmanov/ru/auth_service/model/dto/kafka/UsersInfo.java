package nesalmanov.ru.auth_service.model.dto.kafka;


import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UsersInfo {

    private UUID id;
    private String username;
    private String avatar;

}
