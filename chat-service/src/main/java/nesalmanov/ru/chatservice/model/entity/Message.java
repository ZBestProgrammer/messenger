package nesalmanov.ru.chatservice.model.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "message_id")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    private Chat chatId;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private ExtObject senderId;

    private String content;

    private OffsetDateTime sentAt;

}
