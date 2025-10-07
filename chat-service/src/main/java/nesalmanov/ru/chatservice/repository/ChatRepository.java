package nesalmanov.ru.chatservice.repository;

import nesalmanov.ru.chatservice.model.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import java.util.UUID;

@Repository
public interface ChatRepository extends JpaRepository<Chat, UUID> {
    @Query("select uc.chatId from UserChat uc where uc.userId.extId = :userId")
    List<Chat> findAllChatsByUserId(@Param("userId") UUID userId);

    @Query("select uc.userId.extId from UserChat uc where uc.chatId.chatId = :chatId and uc.userId.extId <> :currentUserId")
    List<UUID> findAllUserIdsByChatIdExceptUser(@Param("chatId") UUID chatId, @Param("currentUserId") UUID currentUserId);
}
