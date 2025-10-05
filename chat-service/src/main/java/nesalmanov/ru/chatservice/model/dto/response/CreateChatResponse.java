package nesalmanov.ru.chatservice.model.dto.response;


import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CreateChatResponse extends Response{
    private UUID user1;
    private UUID user2;
}
