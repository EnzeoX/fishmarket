package technikal.task.fishmarket.services;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
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
import technikal.task.fishmarket.dto.UserForm;
import technikal.task.fishmarket.entity.UserEntity;
import technikal.task.fishmarket.enums.Role;
import technikal.task.fishmarket.handler.UserHandler;
import technikal.task.fishmarket.repository.UserRepository;
import technikal.task.fishmarket.utils.JwtUtils;

/**
 * @author Nikolay Boyko
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserHandler userHandler;
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

    public Cookie authorizeUserWithCookie(UserForm userForm) {
        return userHandler.authUserCookie(userForm);
    }

    public String authorizeUserWithToken(UserForm userForm) {
        return userHandler.authUserToken(userForm);
    }

    public UserEntity getUserByUsername(String username) {
        return userRepository.findUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public void authUser(UserForm userForm) {
        userHandler.authUser(userForm);
    }
}
