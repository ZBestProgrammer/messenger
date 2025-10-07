package nesalmanov.ru.chatservice.model.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class CreateChatRequest {
    private List<UUID> users;
    private String name;
    private String avatar;
}
