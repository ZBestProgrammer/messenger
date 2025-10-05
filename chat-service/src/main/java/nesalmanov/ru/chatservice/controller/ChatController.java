package nesalmanov.ru.chatservice.controller;

import nesalmanov.ru.chatservice.model.dto.request.CreateChatRequest;
import nesalmanov.ru.chatservice.model.dto.response.Response;
import nesalmanov.ru.chatservice.model.entity.Chat;
import nesalmanov.ru.chatservice.service.ChatService;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/createChat")
    public Response createChat(@RequestBody CreateChatRequest createChatRequest) {
        return chatService.createChat(createChatRequest);
    }

}
