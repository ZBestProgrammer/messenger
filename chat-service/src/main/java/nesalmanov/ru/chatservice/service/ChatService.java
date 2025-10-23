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
import nesalmanov.ru.chatservice.model.dto.websocket.ChatMessageDTO;
import nesalmanov.ru.chatservice.model.entity.Chat;
import nesalmanov.ru.chatservice.model.entity.ExtObject;
import nesalmanov.ru.chatservice.model.entity.Message;
import nesalmanov.ru.chatservice.model.entity.UserChat;
import nesalmanov.ru.chatservice.model.impl.TokenDetails;
import nesalmanov.ru.chatservice.repository.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Service
@Slf4j
public class ChatService {

    private final ChatRepository chatRepository;

    private final MessageRepository messageRepository;

    private final ExtObjectRepository extObjectRepository;

    private final GenericTypeRepository genericTypeRepository;

    private final UserChatRepository userChatRepository;

    private final UserUuidKafkaProducer userUuidKafkaProducer;

    private final ObjectMapper objectMapper;

    private final ChatMapper chatMapper;

    public ChatService(ChatRepository chatRepository,
                       MessageRepository messageRepository,
                       ExtObjectRepository extObjectRepository,
                       GenericTypeRepository genericTypeRepository,
                       UserChatRepository userChatRepository,
                       UserUuidKafkaProducer userUuidKafkaProducer,
                       ObjectMapper objectMapper,
                       ChatMapper chatMapper
    ) {
        this.chatRepository = chatRepository;
        this.messageRepository = messageRepository;
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

        UserChatsResponse userChatsResponse = objectMapper.readValue(userResponses, UserChatsResponse.class);

        for (UserChatsInfo chatsInfo : userChatsResponse.getUsersInfo()) {
            Optional<UUID> chatUuid = userChatRepository.findChatIdByUsers(tokenDetails.getUuid(), chatsInfo.getId());

            chatUuid.ifPresent(chatsInfo::setChatId);
        }

        return userChatsResponse.getUsersInfo();

    }

    public List<String> getRecipientsInChat(UUID chatId, UUID senderId) {
        return chatRepository.findAllUserIdsByChatIdExceptUser(chatId, senderId)
                .stream()
                .map(UUID::toString)
                .toList();
    }

    public void saveMessage(ChatMessageDTO chatMessageDTO) {
        Message message = new Message();
        Chat chat = chatRepository.findChatByChatId(chatMessageDTO.getChatId());
        message.setChatId(chat);
        Optional<ExtObject> extObject = extObjectRepository.findByExtId(chatMessageDTO.getSenderId());
        message.setSenderId(extObject.get());
        message.setContent(chatMessageDTO.getContent());
        message.setSentAt(OffsetDateTime.now());

        messageRepository.save(message);
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
