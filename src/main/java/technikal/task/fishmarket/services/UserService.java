package technikal.task.fishmarket.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import technikal.task.fishmarket.handler.UserActionHandler;

/**
 * @author Nikolay Boyko
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserActionHandler userActionHandler;
    private final AuthenticationManager authenticationManager;

    public void registerUser(String username, String password) {
        userActionHandler.saveUserData(username, password);
        authUser(username, password);
    }

    /**
     * @param username Provided username
     * @param password Provided password
     */
    public void authUser(String username, String password) {
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                username, password
        ));

        if (!auth.isAuthenticated()) throw new UsernameNotFoundException("User not authenticated");
        UserDetails userDetails = userActionHandler.loadUserByUsername(username);
        if (!userDetails.isAccountNonExpired()) throw new AccountExpiredException("User expired");
    }

    public UserDetails getUserDetails(String username) {
        return userActionHandler.loadUserByUsername(username);
    }
}
