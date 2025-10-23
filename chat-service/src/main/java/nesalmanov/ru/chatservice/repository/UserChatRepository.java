package nesalmanov.ru.chatservice.repository;


import nesalmanov.ru.chatservice.model.entity.UserChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserChatRepository extends JpaRepository<UserChat, UUID> {
    @Query("""
        SELECT uc1.chatId.chatId
        FROM UserChat uc1
        WHERE uc1.userId.extId = :currentUserId
          AND EXISTS (
              SELECT 1 FROM UserChat uc2
              WHERE uc2.chatId = uc1.chatId
                AND uc2.userId.extId = :targetUserId
          )
    """)
    Optional<UUID> findChatIdByUsers(@Param("currentUserId") UUID currentUserId,
                                     @Param("targetUserId") UUID targetUserId);
}
