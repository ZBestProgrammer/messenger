package nesalmanov.ru.chatservice.repository;


import nesalmanov.ru.chatservice.model.entity.Chat;
import nesalmanov.ru.chatservice.model.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<Message, UUID> {
    List<Message> findAllByChatId(Chat chat);
}
