package nesalmanov.ru.chatservice.repository;


import nesalmanov.ru.chatservice.model.entity.GenericType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GenericTypeRepository extends JpaRepository<GenericType, UUID> {
    GenericType findGenericTypeByName(String name);
}
