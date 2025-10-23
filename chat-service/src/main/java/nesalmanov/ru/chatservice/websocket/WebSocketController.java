package nesalmanov.ru.chatservice.websocket;


import lombok.extern.slf4j.Slf4j;
import nesalmanov.ru.chatservice.model.dto.websocket.ChatMessageDTO;
import nesalmanov.ru.chatservice.service.ChatService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@Slf4j
public class WebSocketController {

        private final SimpMessagingTemplate simpMessagingTemplate;

        private final ChatService chatService;

    public WebSocketController(SimpMessagingTemplate simpMessagingTemplate, ChatService chatService) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.chatService = chatService;
    }

    @MessageMapping("/chat.sendMessage")
        public void sendMessage(ChatMessageDTO messageDTO) {

        chatService.saveMessage(messageDTO);

        List<String> recipients = chatService.getRecipientsInChat(
                messageDTO.getChatId(), messageDTO.getSenderId()
        );
        log.info(messageDTO.getContent());
        for (String userId : recipients) {
            if (!userId.equals(messageDTO.getSenderId().toString())) {
                simpMessagingTemplate.convertAndSendToUser(
                        userId,
                        "/queue/chat/" + messageDTO.getChatId(),
                        messageDTO
                );
            }
        }
    }

}
