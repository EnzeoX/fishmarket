package technikal.task.fishmarket.config.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import technikal.task.fishmarket.utils.JwtUtils;

import java.io.IOException;

/**
 * @author Nikolay Boyko
 */

@Slf4j
@Setter
//@Component
@RequiredArgsConstructor
public class JwtTokenGeneratorFilter
//        extends AbstractAuthenticationProcessingFilter
//    extends UsernamePasswordAuthenticationFilter
{

//    private static final RequestMatcher requestMatcher = new AntPathRequestMatcher("/user/login", HttpMethod.POST.name());
//    private SessionAuthenticationStrategy sessionStrategy = new NullAuthenticatedSessionStrategy();
    private boolean continueChainBeforeSuccessfulAuthentication = false;

    private final JwtUtils jwtUtils;

    private final String firstParameter = "username";
    private final String secondParameter = "password";

    private AuthenticationSuccessHandler successHandler;

//    @Autowired
//    protected JwtTokenGeneratorFilter(JwtUtils jwtUtils, AuthenticationManager authenticationManager) {
//        super(requestMatcher, authenticationManager);
//        this.jwtUtils = jwtUtils;
//    }

//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        this.doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
//    }
//
//    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
//        if (!this.requiresAuthentication(request, response)) {
//            chain.doFilter(request, response);
//        } else {
//            try {
//                Authentication authenticationResult = attemptAuthentication(request, response);
//                if (authenticationResult == null) {
//                    return;
//                }
//                log.info("Authentication info: {}", authenticationResult);
//                this.sessionStrategy.onAuthentication(authenticationResult, request, response);
//                if (this.continueChainBeforeSuccessfulAuthentication) {
//                    chain.doFilter(request, response);
//                }
//
//                successfulAuthentication(request, response, chain, authenticationResult);
//            } catch (InternalAuthenticationServiceException var5) {
//                this.logger.error("An internal error occurred while trying to authenticate the user.", var5);
//                this.unsuccessfulAuthentication(request, response, var5);
//            } catch (AuthenticationException var6) {
//                this.unsuccessfulAuthentication(request, response, var6);
//            }
//
//        }
//    }

//    private boolean shouldNotFilter(HttpServletRequest request) {
//        boolean val = requestMatcher.matches(request);
//        log.info("shouldNotFilter: {}", val);
//        return !val;
//    }

//    @Override
//    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
////        if (shouldNotFilter(request)) {
////            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
////        } else {
//            String username = obtainUsername(request);
//            username = username != null ? username.trim() : "";
//            String password = obtainPassword(request);
//            password = password != null ? password : "";
//            UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken.unauthenticated(username, password);
//            setDetails(request, authRequest);
//            return this.getAuthenticationManager().authenticate(authRequest);
////        }
//    }
//
////    @Nullable
////    protected String obtainPassword(HttpServletRequest request) {
////        return request.getParameter(secondParameter);
////    }
//
////    @Nullable
////    private String obtainUsername(HttpServletRequest request) {
////        return request.getParameter(firstParameter);
////    }
////
////    private void setDetails(HttpServletRequest request, UsernamePasswordAuthenticationToken authRequest) {
////        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
////    }
//
//    @Override
//    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
//        String token = jwtUtils.generateToken(authResult.getName());
//        if (token == null) throw new NullPointerException("Token not generated");
//        this.successHandler.onAuthenticationSuccess(request, response, authResult);
////        super.successfulAuthentication(request, response, chain, authResult);
//    }
//
//    @Override
//    public void setAuthenticationSuccessHandler(AuthenticationSuccessHandler successHandler) {
////        super.setAuthenticationSuccessHandler(successHandler);
//        this.successHandler = successHandler;
//    }
}
