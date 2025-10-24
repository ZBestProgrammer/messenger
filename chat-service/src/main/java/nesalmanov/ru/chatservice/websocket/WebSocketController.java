package nesalmanov.ru.chatservice.websocket;


import lombok.extern.slf4j.Slf4j;
import nesalmanov.ru.chatservice.model.dto.websocket.ChatMessageDTO;
import nesalmanov.ru.chatservice.model.impl.TokenDetails;
import nesalmanov.ru.chatservice.service.ChatService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;

import java.security.Principal;
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
    public void sendMessage(ChatMessageDTO messageDTO, Principal principal) {
        if (principal instanceof UsernamePasswordAuthenticationToken) {
            Object principalObj = ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
            if (principalObj instanceof TokenDetails tokenDetails) {
                chatService.saveMessage(messageDTO, tokenDetails.getUuid());

                List<String> recipients = chatService.getRecipientsInChat(
                        messageDTO.getChatId(), tokenDetails.getUuid()
                );
                log.info(messageDTO.getContent());
                for (String userId : recipients) {
                    if (!userId.equals(tokenDetails.getUuid().toString())) {
                        simpMessagingTemplate.convertAndSendToUser(
                                userId,
                                "/queue/chat/" + messageDTO.getChatId(),
                                messageDTO
                        );
                    }
                }
            }
        }
    }

}
