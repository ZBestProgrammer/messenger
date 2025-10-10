package nesalmanov.ru.chatservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import nesalmanov.ru.chatservice.model.dto.kafka.UserChatsInfo;
import nesalmanov.ru.chatservice.model.dto.request.CreateChatRequest;
import nesalmanov.ru.chatservice.model.dto.response.Response;
import nesalmanov.ru.chatservice.service.ChatService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping("/api")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/getChats")
    public List<UserChatsInfo> getChats() throws ExecutionException, InterruptedException, TimeoutException, JsonProcessingException {
        return chatService.getChats();
    }

    @PostMapping("/createChat")
    public Response createChat(@RequestBody CreateChatRequest createChatRequest) {
        return chatService.createChat(createChatRequest);
    }

}
