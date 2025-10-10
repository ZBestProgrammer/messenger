package nesalmanov.ru.auth_service.model.dto.kafka;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UsersInfoResponse {

    List<UsersInfo> usersInfo;

}