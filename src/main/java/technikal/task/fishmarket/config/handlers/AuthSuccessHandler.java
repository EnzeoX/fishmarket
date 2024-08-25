package technikal.task.fishmarket.config.handlers;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import technikal.task.fishmarket.utils.JwtUtils;

import java.io.IOException;

/**
 * @author Nikolay Boyko
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtils jwtUtils;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
//        String username = authentication.getName();
//        String jwtToken = jwtUtils.generateToken(username);
//        log.info("Auth was successful, setting cookie");
//        Cookie cookie = new Cookie(JwtUtils.JWT_COOKIE_NAME, jwtToken);
//        cookie.setHttpOnly(true); // To prevent XSS attacks
//        cookie.setSecure(true);   // Use true in production for HTTPS
//        cookie.setPath(request.getContextPath());
//        cookie.setMaxAge(JwtUtils.accessTokenValidity); // 10 hours
//
//        response.addCookie(cookie);
        response.sendRedirect("/fish");
    }
}
