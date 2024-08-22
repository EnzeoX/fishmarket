package technikal.task.fishmarket.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import technikal.task.fishmarket.entity.User;
import technikal.task.fishmarket.enums.Role;
import technikal.task.fishmarket.repository.UserRepository;
import technikal.task.fishmarket.utils.JwtUtils;

/**
 * @author Nikolay Boyko
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class UserActionHandler implements UserDetailsService {

    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public void saveUserData(String username, String password) {
        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .role(Role.USER)
                .build();
        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username == null || username.isEmpty()) {
            throw new UsernameNotFoundException("username not provided");
        }
        return userRepository.findUserByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException(String.format("User by username \"%s\" not found", username)));
    }
}
