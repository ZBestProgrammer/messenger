package nesalmanov.ru.chatservice.service;

import nesalmanov.ru.chatservice.mapper.ChatMapper;
import nesalmanov.ru.chatservice.model.dto.request.CreateChatRequest;
import nesalmanov.ru.chatservice.model.dto.response.Response;
import nesalmanov.ru.chatservice.model.entity.Chat;
import nesalmanov.ru.chatservice.model.impl.ChatDetails;
import nesalmanov.ru.chatservice.repository.ChatRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ChatService {

    private final ChatRepository chatRepository;

    private final ChatMapper chatMapper;

    public ChatService(ChatRepository chatRepository, ChatMapper chatMapper) {
        this.chatRepository = chatRepository;
        this.chatMapper = chatMapper;
    }

    public List<Chat> getChats() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ChatDetails chatDetails = (ChatDetails) authentication.getPrincipal();
        return chatRepository.findAllByUser1(chatDetails.getUuid());
    }

    public Response createChat(CreateChatRequest createChatRequest) {
        UUID user1 = createChatRequest.getUser1();
        UUID user2 = createChatRequest.getUser2();
        Optional<Chat> chat = chatRepository.findChatByUser1AndUser2(user1, user2);

        if (chat.isEmpty()) {
            Chat newChat = chatMapper.createChatRequestToChat(createChatRequest);
            Chat savedChat = chatRepository.save(newChat);
            return chatMapper.chatToCreateChatResponse(savedChat);
        }

        return Response.isError("Chat already exist");
    }
}
