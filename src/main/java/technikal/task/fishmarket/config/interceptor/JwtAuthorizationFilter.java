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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import technikal.task.fishmarket.services.CustomUserDetailsService;
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
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtil;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            log.info("Cookie: {}, {}", cookie.getName(), cookie.getValue());
        }
        Optional<Cookie> jwtCookie = Arrays.stream(cookies)
                .filter(cookie -> JwtUtils.JWT_COOKIE_NAME.equals(cookie.getName()))
                .findFirst();

        if (jwtCookie.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        jwtCookie.ifPresent(value -> {
            String token = value.getValue();
            String username = jwtUtil.extractUsername(token);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if (jwtUtil.isTokenValid(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails.getUsername(),
                            null,
                            userDetails.getAuthorities()
                    );
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        });

        filterChain.doFilter(request, response);
    }
}
