package technikal.task.fishmarket.config.interceptor;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import technikal.task.fishmarket.services.UserService;
import technikal.task.fishmarket.utils.JwtUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

/**
 * @author Nikolay Boyko
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";

    private final JwtUtils jwtUtil;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Cookie[] cookies = request.getCookies();
        log.info(Arrays.toString(cookies));
        Optional<Cookie> jwtCookie = Arrays.stream(cookies)
                .filter(cookie -> "jwt".equals(cookie.getName()))
                .findFirst();

        if (jwtCookie.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }


        jwtCookie.ifPresent(value -> {
            String token = value.getValue();
            String username = jwtUtil.extractUsername(token);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                try {
                    UserDetails userDetails = this.userService.getUserDetails(username);
                    if (jwtUtil.isTokenValid(token, userDetails)) {
                        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                                userDetails.getUsername(),
                                userDetails.getPassword(),
                                userDetails.getAuthorities()
                        );
                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    }
                } catch (UsernameNotFoundException e) {
                    log.error(e.getMessage());
                    Cookie cookie = new Cookie("jwt", null);
                    cookie.setHttpOnly(true);
                    cookie.setSecure(true);
                    cookie.setPath("/");
                    cookie.setMaxAge(0);

                    response.addCookie(cookie);
                }
            }
        });

        filterChain.doFilter(request, response);
    }
}
