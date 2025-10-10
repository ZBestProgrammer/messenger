package nesalmanov.ru.auth_service.kafka.producer;


import nesalmanov.ru.auth_service.model.dto.kafka.UsersInfoResponse;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserResponseKafkaProducer {

    private final KafkaTemplate<String, UsersInfoResponse> kafkaTemplate;

    public UserResponseKafkaProducer(KafkaTemplate<String, UsersInfoResponse> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendUserResponseToKafka(UsersInfoResponse users, byte[] correlationId) {
        ProducerRecord<String, UsersInfoResponse> record = new ProducerRecord<>(
                "response-user-info",
                null,
                null,
                null,
                users,
                Collections.singletonList(new RecordHeader(KafkaHeaders.CORRELATION_ID, correlationId))
        );
        kafkaTemplate.send(record);
    }

}
