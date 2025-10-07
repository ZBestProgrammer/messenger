package nesalmanov.ru.chatservice.model.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "users_chats")
public class UserChat {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private ExtObject userId;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    private Chat chatId;

}
