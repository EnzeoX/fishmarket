package technikal.task.fishmarket.config;

import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import technikal.task.fishmarket.config.filters.JwtAuthorizationFilter;
import technikal.task.fishmarket.config.filters.JwtTokenGeneratorFilter;
import technikal.task.fishmarket.config.handlers.AccessDeniedImplHandler;
import technikal.task.fishmarket.config.handlers.AuthSuccessHandler;
import technikal.task.fishmarket.config.handlers.LoginSuccessHandler;
import technikal.task.fishmarket.config.handlers.LogoutHandler;
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

    private final LogoutHandler logoutHandler;
//    private final JwtAuthorizationFilter filter;
    private final UserDetailsService userDetailsService;
    private final AuthSuccessHandler authSuccessHandler;
//    private final AuthenticationProvider authenticationProvider;
    private final FilterExceptionInterceptor filterExceptionInterceptor;



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity
//                                                   AuthenticationManager manager,
//                                                   JwtTokenGeneratorFilter jwtTokenGeneratorFilter
    ) throws Exception {
//        jwtTokenGeneratorFilter.setAuthenticationManager(manager);
//        jwtTokenGeneratorFilter.setAuthenticationSuccessHandler(authSuccessHandler);
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
                            .anyRequest().authenticated()
                    )
                    .formLogin(form -> form
                            .loginPage("/user/login")
                            .successForwardUrl("/fish")
                            .failureUrl("/user/login?error=true")
                            .permitAll())
                    .logout(logout -> logout
                            .logoutUrl("/user/logout")
                            .deleteCookies("JSESSIONID")
                            .logoutSuccessUrl("/fish")
                            .invalidateHttpSession(true)
//                            .logoutSuccessHandler(logoutHandler)
                            .permitAll()
                    )
                    .authenticationProvider(authenticationProvider())
                    .addFilterBefore(filterExceptionInterceptor, BasicAuthenticationFilter.class);
//                    .exceptionHandling(exception -> exception
//                            .accessDeniedPage("error/unauthorizedError"));
//                    .exceptionHandling(exception -> exception
//                    .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)));
//                    .sessionManagement(managementConfigurer -> managementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//                    .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
//                    .addFilterBefore(filterExceptionInterceptor, LogoutFilter.class)
//                    .addFilter(jwtTokenGeneratorFilter);


//                    .csrf(AbstractHttpConfigurer::disable)
////                    .headers(header -> header.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
//                    .authorizeHttpRequests(auth -> auth
//                            .requestMatchers(ALLOWED_URLS).permitAll()
//                            .requestMatchers(HttpMethod.GET, "/user/**").permitAll()
//                            .requestMatchers(HttpMethod.POST, "/user/**").permitAll()
//                            .requestMatchers(HttpMethod.GET, "/fish/create").hasRole("ADMIN")
//                            .requestMatchers(HttpMethod.POST, "/fish/create").hasRole("ADMIN")
//                            .anyRequest().authenticated()
//                    )
//                    .authenticationProvider(authenticationProvider)
////                    .logout(logout -> logout
////                            .logoutUrl("/user/logout")
////                            .logoutSuccessUrl("/user/login")
////                    )
//                    .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                    .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
//                    .addFilterBefore(filterExceptionInterceptor, LogoutFilter.class)
//                    .exceptionHandling(exception -> exception
//                    .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)));

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

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
