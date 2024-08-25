package technikal.task.fishmarket.exception;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class FilterExceptionHandler {

    public void handleError(HttpServletRequest request, HttpServletResponse response, RuntimeException e) throws IOException {
        log.error("{}, error {}", this.getClass().getSimpleName(), e.getClass().getSimpleName());
        if (e.getStackTrace() != null && e.getStackTrace().length > 0) {
            for (StackTraceElement element : e.getStackTrace()) {
                log.error(element.toString());
            }
        }
        switch (e.getClass().getSimpleName()) {
            case "ExpiredJwtException":
                log.error("Token expired");
                request.setAttribute("error", "Сессія не валідна");
                response.addCookie(getEmptyCookie());
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.sendRedirect("/user/login");
                break;
            case "MalformedJwtException":
            case "AuthenticationException":

            case "UsernameNotFoundException":
            case "UnauthorizedUserException":
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.sendRedirect("/fish");
                break;
            default:
                break;
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
