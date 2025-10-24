package nesalmanov.ru.chatservice.mapper;


import nesalmanov.ru.chatservice.model.dto.websocket.ChatMessageDTO;
import nesalmanov.ru.chatservice.model.entity.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MessageMapper {

    @Mapping(source = "chatId.chatId", target = "chatId")
    @Mapping(source = "senderId.extId", target = "senderId")
    @Mapping(source = "sentAt", target = "time")
    ChatMessageDTO toDto(Message message);
    List<ChatMessageDTO> toDtoList(List<Message> messages);

}
