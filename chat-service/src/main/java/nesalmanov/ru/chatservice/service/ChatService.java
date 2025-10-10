package nesalmanov.ru.chatservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import nesalmanov.ru.chatservice.kafka.reply_producer.UserUuidKafkaProducer;
import nesalmanov.ru.chatservice.mapper.ChatMapper;
import nesalmanov.ru.chatservice.model.dto.kafka.UserChatsInfo;
import nesalmanov.ru.chatservice.model.dto.kafka.UserChatsResponse;
import nesalmanov.ru.chatservice.model.dto.request.CreateChatRequest;
import nesalmanov.ru.chatservice.model.dto.kafka.UsersUuidRequest;
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

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Service
@Slf4j
public class ChatService {

    private final ChatRepository chatRepository;

    private final ExtObjectRepository extObjectRepository;

    private final GenericTypeRepository genericTypeRepository;

    private final UserChatRepository userChatRepository;

    private final UserUuidKafkaProducer userUuidKafkaProducer;

    private final ObjectMapper objectMapper;

    private final ChatMapper chatMapper;

    public ChatService(ChatRepository chatRepository,
                       ExtObjectRepository extObjectRepository,
                       GenericTypeRepository genericTypeRepository,
                       UserChatRepository userChatRepository,
                       UserUuidKafkaProducer userUuidKafkaProducer,
                       ObjectMapper objectMapper,
                       ChatMapper chatMapper
    ) {
        this.chatRepository = chatRepository;
        this.extObjectRepository = extObjectRepository;
        this.genericTypeRepository = genericTypeRepository;
        this.userChatRepository = userChatRepository;
        this.userUuidKafkaProducer = userUuidKafkaProducer;
        this.objectMapper = objectMapper;
        this.chatMapper = chatMapper;
    }

    public List<UserChatsInfo> getChats() throws ExecutionException, InterruptedException, TimeoutException, JsonProcessingException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        TokenDetails tokenDetails = (TokenDetails) authentication.getPrincipal();

        List<Chat> chats = chatRepository.findAllChatsByUserId(tokenDetails.getUuid());
        List<UUID> users = new ArrayList<>();

        for (Chat chat : chats) {
            if (chat.getName().isEmpty()) {
                users.addAll(chatRepository.findAllUserIdsByChatIdExceptUser(chat.getChatId(), tokenDetails.getUuid()));
            }
        }

        UsersUuidRequest usersUuid = new UsersUuidRequest();
        usersUuid.setUsers(users);

        String userResponses = userUuidKafkaProducer.sendUserUuidToKafka(usersUuid);



        return objectMapper.readValue(userResponses, UserChatsResponse.class).getUsersInfo();

    }

    @Transactional
    public Response createChat(CreateChatRequest createChatRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        TokenDetails chatDetails = (TokenDetails) authentication.getPrincipal();

        List<UUID> users = createChatRequest.getUsers();
        users.add(chatDetails.getUuid());

        if (users.size() == 2) {
            List<Chat> currentUserChats = chatRepository.findAllChatsByUserId(chatDetails.getUuid());
            List<Chat> userChats = chatRepository.findAllChatsByUserId(users.get(0));

            for (Chat chat : userChats) {
                if (currentUserChats.contains(chat)) {
                    return Response.isError("Chat already exist");
                }
            }
        }

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
