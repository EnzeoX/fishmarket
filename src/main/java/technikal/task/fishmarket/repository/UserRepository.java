package technikal.task.fishmarket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import technikal.task.fishmarket.entity.User;

/**
 * @author Nikolay Boyko
 */
public interface UserRepository extends JpaRepository<User, Integer> {

}
