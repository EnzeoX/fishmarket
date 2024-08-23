package technikal.task.fishmarket.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import technikal.task.fishmarket.entity.UserEntity;
import technikal.task.fishmarket.repository.UserRepository;

/**
 * @author Nikolay Boyko
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Користувача з юзернеймом " + username + " не знайдено!"));
        return User.builder()
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .accountExpired(false)
                .authorities(userEntity.getRole().name())
                .build();
    }
}
