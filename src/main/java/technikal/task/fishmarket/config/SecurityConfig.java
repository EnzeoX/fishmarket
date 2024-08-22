package technikal.task.fishmarket.config;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import technikal.task.fishmarket.config.interceptor.AuthHandler;
import technikal.task.fishmarket.config.interceptor.JwtAuthenticationFilter;
import technikal.task.fishmarket.config.interceptor.LogoutHandler;

/**
 * @author Nikolay Boyko
 */

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthHandler authHandler;
    private final LogoutHandler logoutHandler;

    @Value("${spring.security.enabled}")
    private boolean isSecurityEnabled;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        if (isSecurityEnabled) {
            log.info("Spring security enabled");
            http
                    .csrf(AbstractHttpConfigurer::disable)
                    .headers(header -> header
                            .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin
                            ))
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers( "/favicon.ico").permitAll()
                            .requestMatchers("/h2-console/**").permitAll()
                            .requestMatchers("/images/**").permitAll()
                            .requestMatchers(HttpMethod.GET, "/api/v1/user/**").permitAll()
                            .requestMatchers(HttpMethod.POST, "/api/v1/user/**").permitAll()
                            .requestMatchers(HttpMethod.POST, "/fish/create").hasAuthority("ADMIN")
                            .requestMatchers(HttpMethod.GET, "/fish/create").hasAuthority("ADMIN")
                            .requestMatchers("/fish").permitAll()
                            .anyRequest().authenticated()
                    )
                    .formLogin(form -> form
                            .loginPage("/api/v1/user/login")
                            .successHandler(authHandler)
                            .permitAll())
                    .logout(logout -> logout
                            .logoutUrl("/api/v1/user/logout")
                            .logoutSuccessHandler(logoutHandler)
                            .permitAll()
                    )
                    .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        } else {
            http
                    .csrf(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests(auth -> auth
                            .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
                            .requestMatchers("/images/").permitAll()
                            .requestMatchers(HttpMethod.GET, "/**").permitAll()
                            .requestMatchers(HttpMethod.POST, "/**").permitAll()
                    )
                    .httpBasic(AbstractHttpConfigurer::disable);
        }
        return http.build();
    }
}
