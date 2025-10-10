package nesalmanov.ru.chatservice.model.dto.kafka;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserChatsResponse {

    private List<UserChatsInfo> usersInfo;

}
