package nesalmanov.ru.chatservice.service;

import nesalmanov.ru.chatservice.model.dto.Chat;
import nesalmanov.ru.chatservice.model.impl.ChatDetails;
import nesalmanov.ru.chatservice.repository.ChatRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatService {

    private final ChatRepository chatRepository;

    public ChatService(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    public List<Chat> getChats() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ChatDetails chatDetails = (ChatDetails) authentication.getPrincipal();
        return chatRepository.findAllByUser1(chatDetails.getUuid());
    }
}
