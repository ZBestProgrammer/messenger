package nesalmanov.ru.chatservice.model.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CreateChatRequest {
    private UUID user1;
    private UUID user2;
}
