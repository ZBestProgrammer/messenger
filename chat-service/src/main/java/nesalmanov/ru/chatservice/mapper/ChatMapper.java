package nesalmanov.ru.chatservice.mapper;


import nesalmanov.ru.chatservice.model.dto.request.CreateChatRequest;
import nesalmanov.ru.chatservice.model.dto.response.CreateChatResponse;
import nesalmanov.ru.chatservice.model.entity.Chat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ChatMapper {

    @Mapping(source = "user1", target = "user1")
    @Mapping(source = "user2", target = "user2")
    CreateChatResponse chatToCreateChatResponse(Chat chat);

    @Mapping(source = "user1", target = "user1")
    @Mapping(source = "user2", target = "user2")
    Chat createChatRequestToChat(CreateChatRequest createChatRequest);
}
