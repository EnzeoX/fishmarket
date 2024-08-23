package technikal.task.fishmarket.services;

import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    public void registerUser(String username, String password) {
        UserEntity userEntity = UserEntity.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .role(Role.USER) // make roles in a separate repository for them
                .build();
        userRepository.save(userEntity);
    }

    public UserEntity getUserByUsername(String username) {
        return userRepository.findUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    // Provide cookie here or just provide token and then create cookie in a controller, hmmm
    public Cookie authenticateUser(UserForm userForm) {
        userHandler.authenticateUser(userForm);
        String jwtToken = userHandler.generateTokenForUser(userForm);
        Cookie cookie = new Cookie(JwtUtils.JWT_COOKIE_NAME, jwtToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(JwtUtils.accessTokenValidity);

        return cookie;
    }


}
