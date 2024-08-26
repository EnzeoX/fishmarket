package technikal.task.fishmarket.services;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.hibernate.query.spi.Limit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import technikal.task.fishmarket.dto.UserDto;
import technikal.task.fishmarket.dto.UserForm;
import technikal.task.fishmarket.entity.UserEntity;
import technikal.task.fishmarket.enums.Role;
import technikal.task.fishmarket.exception.exceptions.HighAuthorityException;
import technikal.task.fishmarket.exception.exceptions.UserAuthErrorException;
import technikal.task.fishmarket.handler.UserHandler;
import technikal.task.fishmarket.repository.UserRepository;
import technikal.task.fishmarket.utils.JwtUtils;

import java.util.List;

/**
 * @author Nikolay Boyko
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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
        userRepository.save(entity);
        log.info("Default user {} added", entity);
        userRepository.save(entityAdmin);
        log.info("Default admin user {} added", entityAdmin);

    }

    public void registerUser(String username, String password) {
        UserEntity userEntity = UserEntity.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .role(Role.USER) // make roles in a separate repository for them
                .build();
        userRepository.save(userEntity);
    }

    public UserDto getUserByUsername(String username) {
        UserEntity entity = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User by username not found"));
        return UserDto.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .role(entity.getRole())
                .build();
    }

    public Page<UserDto> getPaginatedUsers(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<UserDto> list;
        List<UserEntity> entities = userRepository.findByOrderByUsernameDesc(pageable);
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
        return new PageImpl<>(list, PageRequest.of(currentPage, pageSize), list.size());
    }

    public void deleteUser(int id) throws HighAuthorityException {
        UserEntity entity = userRepository.findById(id).orElseThrow(
                () -> new UsernameNotFoundException(String.format("No user by id %s found", id)));
        if (entity.getRole().equals(Role.ADMIN)) throw new HighAuthorityException("Can't delete user with this authority");
        userRepository.delete(entity);
    }
}
