package technikal.task.fishmarket.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import technikal.task.fishmarket.config.interceptor.JwtAuthenticationFilter;
import technikal.task.fishmarket.handler.UserActionHandler;
import technikal.task.fishmarket.repository.UserRepository;
import technikal.task.fishmarket.utils.JwtUtils;

/**
 * @author Nikolay Boyko
 */

@Configuration
public class AppConfig {

    @Bean
    public JwtUtils jwtUtils() {
        return new JwtUtils();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return new UserActionHandler(jwtUtils(), passwordEncoder(),  userRepository);
    }

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService) throws Exception {
        return new ProviderManager(authenticationProvider(userDetailsService));
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public FilterRegistrationBean<JwtAuthenticationFilter> jwtAuthenticationFilterRegistrationBean(JwtAuthenticationFilter filter) {
        FilterRegistrationBean<JwtAuthenticationFilter> frb = new FilterRegistrationBean<>(filter);
        frb.setEnabled(false);
        return frb;
    }
}
