package technikal.task.fishmarket.exception;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import technikal.task.fishmarket.utils.JwtUtils;

import java.io.IOException;

/**
 * @author Nikolay Boyko
 */

@Slf4j
@Component
public class FilterExceptionHandler {

    public void handleError(HttpServletRequest request, HttpServletResponse response, Exception e) throws IOException {
        log.error("{}, error {}", this.getClass().getSimpleName(), e.getClass().getSimpleName());
        switch (e.getClass().getSimpleName()) {
            case "ExpiredJwtException" -> {
                request.setAttribute("error", "Сессія не валідна");
                response.addCookie(getEmptyCookie());
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.sendRedirect("/user/login");
            }
            case "AccessDeniedException" -> {
                log.warn("user tried to access endpoint without authorities");
                response.sendRedirect("/fish");
            }
            case "MalformedJwtException",
                    "AuthenticationException",
                    "UserNotFoundException",
                    "UsernameNotFoundException" -> {
                log.info("User not found");
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.sendRedirect("/user/login");
            }
            case "UnauthorizedUserException" -> {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.sendRedirect("/fish");
            }
            default -> log.error("Default error handler");
        }
    }

    private Cookie getEmptyCookie() {
        Cookie cookie = new Cookie(JwtUtils.JWT_COOKIE_NAME, null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        return cookie;
    }
}
