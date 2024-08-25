package technikal.task.fishmarket.config.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import technikal.task.fishmarket.utils.JwtUtils;

import java.io.IOException;

/**
 * @author Nikolay Boyko
 */

@Slf4j
@Setter
public class JwtTokenGeneratorFilter extends AbstractAuthenticationProcessingFilter {

    private static final RequestMatcher requestMatcher = new AntPathRequestMatcher("/user/login", HttpMethod.POST.name());

    private final JwtUtils jwtUtils;

    private final String firstParameter = "username";
    private final String secondParameter = "password";

    public JwtTokenGeneratorFilter(AuthenticationManager manager, JwtUtils jwtUtils) {
        super(requestMatcher);
        setAuthenticationManager(manager);
        this.jwtUtils = jwtUtils;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("Authorization attempt");
        String username = obtainUsername(request);
        username = username != null ? username.trim() : "";
        String password = obtainPassword(request);
        password = password != null ? password : "";
        UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken.unauthenticated(username, password);
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        Cookie cookie = new Cookie(JwtUtils.JWT_COOKIE_NAME, jwtUtils.generateToken(authResult.getName()));
        cookie.setMaxAge(60 * 1000);
        cookie.setDomain("localhost");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");

        response.addCookie(cookie);
        response.sendRedirect("/fish");
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    @Nullable
    protected String obtainPassword(HttpServletRequest request) {
        return request.getParameter(secondParameter);
    }

    @Nullable
    private String obtainUsername(HttpServletRequest request) {
        return request.getParameter(firstParameter);
    }
}
