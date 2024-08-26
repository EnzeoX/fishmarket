package technikal.task.fishmarket.config;

import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.context.SecurityContextHolderFilter;
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter;
import technikal.task.fishmarket.config.filters.JwtAuthorizationFilter;
import technikal.task.fishmarket.config.filters.JwtTokenGeneratorFilter;
import technikal.task.fishmarket.config.handlers.CustomLogoutHandler;
import technikal.task.fishmarket.config.interceptor.FilterExceptionInterceptor;
import technikal.task.fishmarket.utils.JwtUtils;

/**
 * @author Nikolay Boyko
 */

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final static String[] ALLOWED_URLS = {"/", "/favicon.ico", "/h2-console/**", "/images/**", "/fish"};

    @Value("${spring.security.enabled}")
    private boolean isSecurityEnabled;

    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;
    private final FilterExceptionInterceptor filterExceptionInterceptor;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, AuthenticationManager manager) throws Exception {
        JwtTokenGeneratorFilter jwtTokenGeneratorFilter = new JwtTokenGeneratorFilter(manager, jwtUtils);
        JwtAuthorizationFilter jwtAuthorizationFilter = new JwtAuthorizationFilter(jwtUtils, userDetailsService);
        if (isSecurityEnabled) {
            httpSecurity
                    .csrf(AbstractHttpConfigurer::disable)
                    .headers(header -> header.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers(ALLOWED_URLS).permitAll()
                            .requestMatchers(HttpMethod.GET, "/user/**").permitAll()
                            .requestMatchers(HttpMethod.POST, "/user/**").permitAll()
                            .requestMatchers(HttpMethod.POST, "/fish/create").hasAuthority("ADMIN")
                            .requestMatchers(HttpMethod.GET, "/fish/create").hasAuthority("ADMIN")
                            .requestMatchers(HttpMethod.GET, "/fish/delete/**").hasAuthority("ADMIN")
                            .requestMatchers("/admin/**").hasAuthority("ADMIN")
                            .anyRequest().authenticated()
                    )
                    .logout(logout -> logout
                            .logoutUrl("/user/logout")
                            .deleteCookies(JwtUtils.JWT_COOKIE_NAME)
                            .logoutSuccessHandler(new CustomLogoutHandler())
                    )
                    .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .exceptionHandling(exception -> exception
                        .accessDeniedPage("/error/403")
                    )
                    .addFilterBefore(jwtTokenGeneratorFilter, UsernamePasswordAuthenticationFilter.class)
                    .addFilterBefore(jwtAuthorizationFilter, JwtTokenGeneratorFilter.class)
                    .addFilterBefore(filterExceptionInterceptor, WebAsyncManagerIntegrationFilter.class);

        } else {
            // configure
            httpSecurity
                    .csrf(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests(auth -> auth
                            .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
                            .requestMatchers("/images/").permitAll()
                            .requestMatchers("/**").permitAll()
                    )
                    .httpBasic(AbstractHttpConfigurer::disable);
        }
        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    //    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
//        return config.getAuthenticationManager();
//    }
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class).build();
    }
}
