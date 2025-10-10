package nesalmanov.ru.auth_service.kafka.consumer;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import nesalmanov.ru.auth_service.kafka.producer.UserResponseKafkaProducer;
import nesalmanov.ru.auth_service.mapper.UserMapper;
import nesalmanov.ru.auth_service.model.dto.kafka.UsersInfo;
import nesalmanov.ru.auth_service.model.dto.kafka.UsersUuidRequest;
import nesalmanov.ru.auth_service.model.dto.kafka.UsersInfoResponse;
import nesalmanov.ru.auth_service.model.entity.User;
import nesalmanov.ru.auth_service.repository.UserRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class UserResponseKafkaConsumer {

    private final UserResponseKafkaProducer userResponseKafkaProducer;

    private final ObjectMapper objectMapper;

    private final UserRepository userRepository;


    private final UserMapper userMapper;

    public UserResponseKafkaConsumer(UserResponseKafkaProducer userResponseKafkaProducer, ObjectMapper objectMapper, UserRepository userRepository, UserMapper userMapper) {
        this.userResponseKafkaProducer = userResponseKafkaProducer;
        this.objectMapper = objectMapper;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @KafkaListener(topics = "request-user-info", groupId = "send-userinfo-group")
    public void consumeUserUuid(ConsumerRecord<String, String> record) throws Exception {

        byte[] correlationId = record.headers().lastHeader(KafkaHeaders.CORRELATION_ID).value();

        UsersUuidRequest users = objectMapper.readValue(record.value(), UsersUuidRequest.class);
        UsersInfoResponse usersResponse = new UsersInfoResponse();
        List<UsersInfo> usersInfos = new ArrayList<>();

        for (UUID user : users.getUsers()) {
            User foundUser = userRepository.findUserById(user);
            usersInfos.add(userMapper.userToUsersInfo(foundUser));
        }

        usersResponse.setUsersInfo(usersInfos);

        userResponseKafkaProducer.sendUserResponseToKafka(usersResponse, correlationId);
    }
}
