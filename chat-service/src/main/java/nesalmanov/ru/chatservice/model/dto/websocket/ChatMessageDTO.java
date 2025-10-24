package nesalmanov.ru.chatservice.model.dto.websocket;

import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;


@Getter
@Setter
public class ChatMessageDTO {

    private UUID chatId;
    private UUID senderId;
    private String content;
    private OffsetDateTime time;

}
