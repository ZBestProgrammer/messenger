package nesalmanov.ru.auth_service.repository;

import nesalmanov.ru.auth_service.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    User findUserByUsername(String username);
    List<User> findByUsernameStartingWith(String username);
}
