package nesalmanov.ru.chatservice.repository;

import nesalmanov.ru.chatservice.model.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChatRepository extends JpaRepository<Chat, UUID> {
    List<Chat> findAllByUser1(UUID user1);
    Optional<Chat> findChatByUser1AndUser2(UUID user1, UUID user2);
}
