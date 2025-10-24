package nesalmanov.ru.chatservice.model.dto.response;


import lombok.Getter;
import lombok.Setter;
import nesalmanov.ru.chatservice.model.dto.websocket.ChatMessageDTO;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class GetMessagesResponse {

    private UUID currentUserId;
    private List<ChatMessageDTO> messages;

}
