package nesalmanov.ru.chatservice.model.dto.request;


import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class GetMessagesRequest {

    private UUID chatId;

}
