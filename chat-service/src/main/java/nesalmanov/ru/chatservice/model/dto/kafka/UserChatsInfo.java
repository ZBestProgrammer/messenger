package nesalmanov.ru.chatservice.model.dto.kafka;


import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UserChatsInfo {

    private UUID id;
    private UUID chatId;
    private String username;
    private String avatar;

}
