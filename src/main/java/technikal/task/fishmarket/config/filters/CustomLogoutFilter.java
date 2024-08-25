package technikal.task.fishmarket.config.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.log.LogMessage;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import technikal.task.fishmarket.utils.JwtUtils;

import java.io.IOException;

/**
 * @author Nikolay Boyko
 */

@Slf4j
//@Component
public class CustomLogoutFilter
//        extends LogoutFilter
{
//
//    private final LogoutSuccessHandler successCustomLogoutHandler;
//    private final LogoutHandler cookieClearingLogoutHandler;
//
//    @Autowired
//    public CustomLogoutFilter(LogoutSuccessHandler logoutSuccessHandler, LogoutHandler... handlers) {
//        super(logoutSuccessHandler, handlers);
//        this.successCustomLogoutHandler = logoutSuccessHandler;
//        this.cookieClearingLogoutHandler = new CookieClearingLogoutHandler(JwtUtils.JWT_COOKIE_NAME);
//        super.setLogoutRequestMatcher(new AntPathRequestMatcher("/user/logout", HttpMethod.POST.name()));
//    }
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        this.doFilter((HttpServletRequest)request, (HttpServletResponse)response, chain);
//    }
//
//    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
//        if (this.requiresLogout(request, response)) {
//            Authentication auth = SecurityContextHolder.getContextHolderStrategy().getContext().getAuthentication();
//            if (this.logger.isDebugEnabled()) {
//                this.logger.debug(LogMessage.format("Logging out [%s]", auth));
//            }
//
//            this.cookieClearingLogoutHandler.logout(request, response, auth);
//            this.successCustomLogoutHandler.onLogoutSuccess(request, response, auth);
//        } else {
//            chain.doFilter(request, response);
//        }
//    }
}
