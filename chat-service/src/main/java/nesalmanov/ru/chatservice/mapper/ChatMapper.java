package nesalmanov.ru.chatservice.mapper;


import nesalmanov.ru.chatservice.model.dto.request.CreateChatRequest;
import nesalmanov.ru.chatservice.model.dto.response.CreateChatResponse;
import nesalmanov.ru.chatservice.model.entity.Chat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ChatMapper {

    @Mapping(source = "chatId", target = "chatId")
    CreateChatResponse chatToCreateChatResponse(Chat chat);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "avatar", target = "avatar")
    Chat createChatRequestToChat(CreateChatRequest createChatRequest);
}
