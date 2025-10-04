package nesalmanov.ru.chatservice.controller;

import nesalmanov.ru.chatservice.model.dto.Chat;
import nesalmanov.ru.chatservice.service.ChatService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/getChats")
    public List<Chat> getChats() {
        return chatService.getChats();
    }

}
