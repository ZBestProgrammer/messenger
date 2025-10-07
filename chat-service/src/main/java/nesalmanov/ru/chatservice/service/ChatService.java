package nesalmanov.ru.chatservice.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import nesalmanov.ru.chatservice.kafka.producer.UserUuidKafkaProducer;
import nesalmanov.ru.chatservice.mapper.ChatMapper;
import nesalmanov.ru.chatservice.model.dto.request.CreateChatRequest;
import nesalmanov.ru.chatservice.model.dto.response.Response;
import nesalmanov.ru.chatservice.model.entity.Chat;
import nesalmanov.ru.chatservice.model.entity.ExtObject;
import nesalmanov.ru.chatservice.model.entity.UserChat;
import nesalmanov.ru.chatservice.model.impl.TokenDetails;
import nesalmanov.ru.chatservice.repository.ChatRepository;
import nesalmanov.ru.chatservice.repository.ExtObjectRepository;
import nesalmanov.ru.chatservice.repository.GenericTypeRepository;
import nesalmanov.ru.chatservice.repository.UserChatRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class ChatService {

    private final ChatRepository chatRepository;

    private final ExtObjectRepository extObjectRepository;

    private final GenericTypeRepository genericTypeRepository;

    private final UserChatRepository userChatRepository;

    private final UserUuidKafkaProducer userUuidKafkaProducer;

    private final ChatMapper chatMapper;

    public ChatService(ChatRepository chatRepository, ExtObjectRepository extObjectRepository, GenericTypeRepository genericTypeRepository, UserChatRepository userChatRepository, UserUuidKafkaProducer userUuidKafkaProducer, ChatMapper chatMapper) {
        this.chatRepository = chatRepository;
        this.extObjectRepository = extObjectRepository;
        this.genericTypeRepository = genericTypeRepository;
        this.userChatRepository = userChatRepository;
        this.userUuidKafkaProducer = userUuidKafkaProducer;
        this.chatMapper = chatMapper;
    }

    public List<Chat> getChats() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        TokenDetails tokenDetails = (TokenDetails) authentication.getPrincipal();

        List<Chat> chats = chatRepository.findAllChatsByUserId(tokenDetails.getUuid());
        List<UUID> users = new ArrayList<>();

        for (Chat chat : chats) {
            if (chat.getName().isEmpty()) {
                users.addAll(chatRepository.findAllUserIdsByChatIdExceptUser(chat.getChatId(), tokenDetails.getUuid()));
            }
        }

        userUuidKafkaProducer.sendUserUuidToKafka(users);

        return chats;

    }

    @Transactional
    public Response createChat(CreateChatRequest createChatRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        TokenDetails chatDetails = (TokenDetails) authentication.getPrincipal();

        List<UUID> users = createChatRequest.getUsers();
        users.add(chatDetails.getUuid());

        Chat newChat = chatMapper.createChatRequestToChat(createChatRequest);
        newChat = chatRepository.save(newChat);
        for (UUID user : users) {
            ExtObject extObject = extObjectRepository.findByExtId(user)
                    .orElseGet(() -> {
                        ExtObject newExtObject = new ExtObject();
                        newExtObject.setExtId(user);
                        newExtObject.setTypeId(genericTypeRepository.findGenericTypeByName("user"));
                        return extObjectRepository.save(newExtObject);
                    });

            UserChat userChat = new UserChat();
            userChat.setUserId(extObject);
            userChat.setChatId(newChat);

            userChatRepository.save(userChat);

        }
        return chatMapper.chatToCreateChatResponse(newChat);
    }
}
