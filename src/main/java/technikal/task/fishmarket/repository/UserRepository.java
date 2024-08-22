package technikal.task.fishmarket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import technikal.task.fishmarket.entity.User;

import java.util.Optional;

/**
 * @author Nikolay Boyko
 */
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findUserByUsername(String username);
}
