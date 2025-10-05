package nesalmanov.ru.auth_service.mapper;

import nesalmanov.ru.auth_service.model.dto.response.UserResponse;
import nesalmanov.ru.auth_service.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse userToUserResponse(User user);

    @Mapping(source = "username", target = "username")
    @Mapping(source = "avatar", target = "avatar")
    List<UserResponse> usersToUserResponses(List<User> users);
}
