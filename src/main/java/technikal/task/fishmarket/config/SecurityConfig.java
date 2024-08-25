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
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import technikal.task.fishmarket.config.interceptor.FilterExceptionInterceptor;

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

    private final UserDetailsService userDetailsService;
    private final FilterExceptionInterceptor filterExceptionInterceptor;



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
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
                            .permitAll()
                    )
                    .authenticationProvider(authenticationProvider())
                    .addFilterBefore(filterExceptionInterceptor, BasicAuthenticationFilter.class);

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
