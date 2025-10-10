package nesalmanov.ru.chatservice.model.dto.kafka;


import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class UsersUuidRequest {

    private List<UUID> users;

    public UsersUuidRequest() {
    }
}
