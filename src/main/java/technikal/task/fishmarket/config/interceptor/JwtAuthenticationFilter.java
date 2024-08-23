package technikal.task.fishmarket.config.interceptor;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import technikal.task.fishmarket.utils.JwtUtils;

import java.io.IOException;

/**
 * @author Nikolay Boyko
 */

//@Slf4j
//@Component
//@RequiredArgsConstructor
public class JwtAuthenticationFilter
//        extends UsernamePasswordAuthenticationFilter
{

//    private final JwtUtils jwtUtils;
//    private final AuthenticationManager authenticationManager;
//
//    @Override
//    public Authentication attemptAuthentication(HttpServletRequest req,
//                                                HttpServletResponse res) throws AuthenticationException {
////            ApplicationUser creds = new ObjectMapper()
////                    .readValue(req.getInputStream(), ApplicationUser.class);
//
//        return authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        creds.getUsername(),
//                        creds.getPassword(),
//                        new ArrayList<>())
//        );
//    }
//
//    @Override
//    protected void successfulAuthentication(HttpServletRequest req,
//                                            HttpServletResponse res,
//                                            FilterChain chain,
//                                            Authentication auth) throws IOException, ServletException {
//
//        String username = auth.getName();
//        String jwtToken = jwtUtils.generateToken(username);
//
//        Cookie cookie = new Cookie(JwtUtils.JWT_COOKIE_NAME, jwtToken);
//        cookie.setHttpOnly(true); // To prevent XSS attacks
//        cookie.setSecure(true);   // Use true in production for HTTPS
//        cookie.setPath("/");
//        cookie.setMaxAge(JwtUtils.accessTokenValidity); // 10 hours
//
//        res.addCookie(cookie);
//        res.sendRedirect("/fish");
//    }
}
