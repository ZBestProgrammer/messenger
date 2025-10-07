package nesalmanov.ru.chatservice.kafka.producer;


import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserUuidKafkaProducer {

    private final KafkaTemplate<String, List<UUID>> kafkaTemplate;

    public UserUuidKafkaProducer(KafkaTemplate<String, List<UUID>> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendUserUuidToKafka(List<UUID> users) {
        kafkaTemplate.send("cs-get-user-info", users);
    }

}
