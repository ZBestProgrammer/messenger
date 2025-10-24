package nesalmanov.ru.chatservice.controller;


import nesalmanov.ru.chatservice.model.dto.response.GetMessagesResponse;
import nesalmanov.ru.chatservice.service.MessageService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/getMessages")
    public GetMessagesResponse getMessages(@RequestParam UUID chatId) {
        return messageService.getMessages(chatId);
    }

}
