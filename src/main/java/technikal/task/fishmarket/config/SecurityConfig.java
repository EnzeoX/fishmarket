package technikal.task.fishmarket.config;

import jakarta.servlet.DispatcherType;
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
import org.springframework.security.web.authentication.logout.LogoutFilter;
import technikal.task.fishmarket.config.interceptor.FilterExceptionInterceptor;
import technikal.task.fishmarket.config.interceptor.JwtAuthorizationFilter;

/**
 * @author Nikolay Boyko
 */

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Value("${spring.security.enabled}")
    private boolean isSecurityEnabled;

    @Bean
    public SecurityFilterChain securityFilterChain(
            JwtAuthorizationFilter jwtAuthorizationFilter,
            HttpSecurity httpSecurity,
            FilterExceptionInterceptor filterExceptionInterceptor) throws Exception {

        if (isSecurityEnabled) {
            httpSecurity
                    .csrf(AbstractHttpConfigurer::disable)
                    .headers(header -> header.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                    .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers("/","/favicon.ico", "/h2-console/**", "/images/**", "/fish").permitAll()
                            .requestMatchers(HttpMethod.GET, "/user/**").permitAll()
                            .requestMatchers(HttpMethod.POST, "/user/**").permitAll()
                            .requestMatchers(HttpMethod.GET, "/fish/create").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.POST, "/fish/create").hasRole("ADMIN")
                            .anyRequest().authenticated()
                    )
//                    .formLogin(form -> form
//                            .loginPage("/user/login")
//                            .failureForwardUrl("/user/login?error")
//                    )
                    .logout(logout -> logout
                            .logoutUrl("/user/logout")
                            .logoutSuccessUrl("/user/login")
                    )
                    .addFilterBefore(filterExceptionInterceptor, LogoutFilter.class)
                    .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);

        } else {
            // configure
            httpSecurity
                    .csrf(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests(auth -> auth
                            .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
                            .requestMatchers("/images/").permitAll()
                            .requestMatchers(HttpMethod.GET, "/**").permitAll()
                            .requestMatchers(HttpMethod.POST, "/**").permitAll()
                    )
                    .httpBasic(AbstractHttpConfigurer::disable);
        }
        return httpSecurity.build();
    }

//    private final CustomUserDetailsService userDetailsService;
//    private final JwtAuthorizationFilter jwtAuthorizationFilter;
//    private final LoginHandler loginHandler;
//    private final LogoutHandler logoutHandler;
//
//    private final AuthenticationProvider authenticationProvider;
//
//    @Value("${spring.security.enabled}")
//    private boolean isSecurityEnabled;
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http, FilterExceptionInterceptor filterExceptionInterceptor) throws Exception {
//        if (isSecurityEnabled) {
//            http
//                    .csrf(AbstractHttpConfigurer::disable)
//                    .headers(header -> header.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
//                    .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                    .authorizeHttpRequests(auth -> auth
//                            .requestMatchers( "/favicon.ico", "/h2-console/**", "/images/**").permitAll()
//                            .requestMatchers(HttpMethod.GET, "/api/v1/user/**").permitAll()
//                            .requestMatchers(HttpMethod.POST, "/api/v1/user/**").permitAll()
//                            .requestMatchers(HttpMethod.POST, "/fish/create").hasAuthority("ADMIN")
//                            .requestMatchers(HttpMethod.GET, "/fish/create").hasAuthority("ADMIN")
//                            .requestMatchers("/fish").permitAll()
//                            .anyRequest().authenticated()
//                    )
//                    .formLogin(form -> form
//                            .loginPage("/api/v1/user/login")
//                            .successHandler(loginHandler)
//                            .successForwardUrl("/fish")
//                        .permitAll()
//                    )
////                    .formLogin(form -> form
////                            .loginPage("/api/v1/user/login")
////                            .failureForwardUrl("/api/v1/user/login")
////                            .successHandler(loginHandler)
////                            .permitAll())
//                    .logout(logout -> logout
//                            .logoutUrl("/api/v1/user/logout")
//                            .deleteCookies(JwtUtils.JWT_COOKIE_NAME)
//                            .logoutSuccessUrl("/api/v1/user/login")
//                            .permitAll()
//                    )
//                    .authenticationProvider(authenticationProvider)
//                    .addFilterBefore(filterExceptionInterceptor, LogoutFilter.class)
//                    .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);
//        } else {
//            http
//                    .csrf(AbstractHttpConfigurer::disable)
//                    .authorizeHttpRequests(auth -> auth
//                            .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
//                            .requestMatchers("/images/").permitAll()
//                            .requestMatchers(HttpMethod.GET, "/**").permitAll()
//                            .requestMatchers(HttpMethod.POST, "/**").permitAll()
//                    )
//                    .httpBasic(AbstractHttpConfigurer::disable);
//        }
//        return http.build();
//    }
}
