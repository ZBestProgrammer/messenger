package nesalmanov.ru.chatservice.kafka.reply_producer;


import nesalmanov.ru.chatservice.model.dto.kafka.UsersUuidRequest;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
public class UserUuidKafkaProducer {

    private final ReplyingKafkaTemplate<String, UsersUuidRequest, String> replyingKafkaTemplate;

    public UserUuidKafkaProducer(ReplyingKafkaTemplate<String, UsersUuidRequest, String> replyingKafkaTemplate) {
        this.replyingKafkaTemplate = replyingKafkaTemplate;
    }

    public String sendUserUuidToKafka(UsersUuidRequest users) throws ExecutionException, InterruptedException, TimeoutException {
        ProducerRecord<String, UsersUuidRequest> record = new ProducerRecord<>("request-user-info", users);
        RequestReplyFuture<String, UsersUuidRequest, String> replyFuture = replyingKafkaTemplate.sendAndReceive(record);

        ConsumerRecord<String, String> consumerRecord = replyFuture.get(10, TimeUnit.SECONDS);
        return consumerRecord.value();
    }

}
