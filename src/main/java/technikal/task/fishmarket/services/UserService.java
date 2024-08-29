package technikal.task.fishmarket.services;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import technikal.task.fishmarket.dto.UserDto;
import technikal.task.fishmarket.entity.UserEntity;
import technikal.task.fishmarket.enums.Role;
import technikal.task.fishmarket.exception.exceptions.HighAuthorityException;
import technikal.task.fishmarket.repository.UserRepository;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Random;

/**
 * @author Nikolay Boyko
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // not a good practice to add test users like that, but anyway
    @PostConstruct
    public void addUser() {
        UserEntity entity = UserEntity.builder()
                .username("1")
                .password(passwordEncoder.encode("1"))
                .role(Role.USER)
                .build();

        UserEntity entityAdmin = UserEntity.builder()
                .username("2")
                .password(passwordEncoder.encode("2"))
                .role(Role.ADMIN)
                .build();
        try {
            userRepository.save(entity);
            log.info("Default user {} added", entity);
            userRepository.save(entityAdmin);
            log.info("Default admin user {} added", entityAdmin);
        } catch (DataIntegrityViolationException e) {
            log.warn("Users already in db");
        }
//        log.info("Adding multiple random users");
//
//        for (int i = 0; i < 50; i++) {
//            byte[] uByte = new byte[10];
//            byte[] pByte = new byte[10];
//            new Random().nextBytes(uByte);
//            new Random().nextBytes(pByte);
//            String username = new String(uByte, StandardCharsets.UTF_8);
//            String password = passwordEncoder.encode(new String(pByte, StandardCharsets.UTF_8));
//            UserEntity randomEnt = UserEntity.builder()
//                    .username(username)
//                    .password(password)
//                    .role(Role.USER)
//                    .build();
//            userRepository.save(randomEnt);
//        }
    }

    public void registerUser(String username, String password) {
        UserEntity userEntity = UserEntity.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .role(Role.USER) // make roles in a separate repository for them
                .build();
        userRepository.save(userEntity);
    }

    public Page<UserDto> getPaginatedUsers(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        long usersCount = userRepository.count();
        List<UserDto> list;
//        List<UserEntity> entities = userRepository.findByOrderByUsernameDesc(pageable);
        List<UserEntity> entities = userRepository.findAll(pageable).getContent();
        if (entities == null || entities.isEmpty()) {
            log.warn("No users in database");
            return Page.empty();
        }
        list = entities.stream()
                .map(entity -> UserDto.builder()
                            .id(entity.getId())
                            .username(entity.getUsername())
                            .role(entity.getRole())
                            .build())
                .toList();
        PageImpl<UserDto> userDtos = new PageImpl<>(list, PageRequest.of(currentPage, pageSize), usersCount);
        return userDtos;
    }

    public void deleteUser(int id) throws HighAuthorityException {
        UserEntity entity = userRepository.findById(id).orElseThrow(
                () -> new UsernameNotFoundException(String.format("No user by id %s found", id)));
        if (entity.getRole().equals(Role.ADMIN)) throw new HighAuthorityException("Can't delete user with this authority");
        userRepository.delete(entity);
    }
}
