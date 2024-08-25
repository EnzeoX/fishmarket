package technikal.task.fishmarket.handler;

import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
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

    public void authUser(UserForm userForm) {
        Authentication auth = manager.authenticate(new UsernamePasswordAuthenticationToken(
                userForm.getUsername(), userForm.getPassword()
        ));
        if (!auth.isAuthenticated()) throw new UserAuthErrorException("User not auth");
    }

    public Cookie authUserCookie(UserForm userForm) {
        authenticateUser(userForm);
        Cookie cookie = new Cookie(JwtUtils.JWT_COOKIE_NAME, jwtUtils.generateToken(userForm.getUsername()));
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(JwtUtils.accessTokenValidity); // 10 hours

        return cookie;
    }

    public String authUserToken(UserForm userForm) {
        authenticateUser(userForm);
        return jwtUtils.generateToken(userForm.getUsername());
    }

    public void authenticateUser(UserForm userForm) {
        Authentication authentication = manager.authenticate(new UsernamePasswordAuthenticationToken(userForm.getUsername(), userForm.getPassword()));
//        SecurityContextHolder.getContext().setAuthentication(authentication);
        if (!authentication.isAuthenticated()) throw new UserAuthErrorException("User not authenticated");
    }

    public ResponseCookie generateResponseCookie(UserForm userForm) {
        return jwtUtils.generateJwtCookie(userForm);
    }
}
