package technikal.task.fishmarket.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import technikal.task.fishmarket.dto.UserForm;
import technikal.task.fishmarket.exception.exceptions.UserAuthErrorException;
import technikal.task.fishmarket.utils.JwtUtils;

/**
 * @author Nikolay Boyko
 */


@Slf4j
@Component
@RequiredArgsConstructor
public class UserHandler {

    private final JwtUtils jwtUtils;
    private final AuthenticationManager manager;

    public String generateTokenForUser(UserForm userForm) {
        return jwtUtils.generateToken(userForm.getUsername());
    }

    public void authenticateUser(UserForm userForm) {
        Authentication authentication = manager.authenticate(new UsernamePasswordAuthenticationToken(userForm.getUsername(), userForm.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        if (!authentication.isAuthenticated()) throw new UserAuthErrorException("User not authenticated");
    }
}
