package technikal.task.fishmarket.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import technikal.task.fishmarket.entity.UserEntity;

import java.util.List;
import java.util.Optional;

/**
 * @author Nikolay Boyko
 */
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    Optional<UserEntity> findUserByUsername(String username);

    List<UserEntity> findByOrderByUsernameDesc(Pageable pageable);
}
