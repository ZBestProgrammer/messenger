package nesalmanov.ru.chatservice.model.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "chats")
public class Chat {

    @Id
    private UUID id;
    private UUID user1;
    private UUID user2;

}
