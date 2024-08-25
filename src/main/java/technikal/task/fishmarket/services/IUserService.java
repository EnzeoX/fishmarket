package technikal.task.fishmarket.services;

import technikal.task.fishmarket.entity.UserEntity;

import java.util.List;

/**
 * @author Nikolay Boyko
 */
public interface IUserService {

    UserEntity findUserByUsername(String username);

    void saveUser(UserEntity userEntity);

    void saveAllUsers(List<UserEntity> userEntityList);

    void deleteUser(UserEntity userEntity);

    void deleteUserByUsername(String username);

    void deleteAllUsers();
}
