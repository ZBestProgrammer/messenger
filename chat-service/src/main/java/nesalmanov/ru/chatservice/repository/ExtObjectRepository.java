package nesalmanov.ru.chatservice.repository;


import nesalmanov.ru.chatservice.model.entity.ExtObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ExtObjectRepository extends JpaRepository<ExtObject, UUID> {
    Optional<ExtObject> findByExtId(UUID id);
}
