package nesalmanov.ru.chatservice.service;


import nesalmanov.ru.chatservice.mapper.MessageMapper;
import nesalmanov.ru.chatservice.model.dto.response.GetMessagesResponse;
import nesalmanov.ru.chatservice.model.dto.websocket.ChatMessageDTO;
import nesalmanov.ru.chatservice.model.entity.Chat;
import nesalmanov.ru.chatservice.model.impl.TokenDetails;
import nesalmanov.ru.chatservice.repository.ChatRepository;
import nesalmanov.ru.chatservice.repository.MessageRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    private final ChatRepository chatRepository;

    private final MessageMapper messageMapper;

    public MessageService(MessageRepository messageRepository, ChatRepository chatRepository, MessageMapper messageMapper) {
        this.messageRepository = messageRepository;
        this.chatRepository = chatRepository;
        this.messageMapper = messageMapper;
    }

    public GetMessagesResponse getMessages(UUID chatId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        TokenDetails tokenDetails = (TokenDetails) authentication.getPrincipal();

        Chat currentChat = chatRepository.findChatByChatId(chatId);

        List<ChatMessageDTO> dtos = messageMapper.toDtoList(messageRepository.findAllByChatId(currentChat));

        GetMessagesResponse messagesResponse = new GetMessagesResponse();
        messagesResponse.setCurrentUserId(tokenDetails.getUuid());
        messagesResponse.setMessages(dtos);

        return messagesResponse;
    }

}
