package technikal.task.fishmarket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import technikal.task.fishmarket.config.filters.CustomLogoutFilter;
import technikal.task.fishmarket.config.filters.JwtAuthorizationFilter;
import technikal.task.fishmarket.config.handlers.CustomLogoutHandler;
import technikal.task.fishmarket.repository.UserRepository;
import technikal.task.fishmarket.services.impl.UserDetailsServiceImpl;
import technikal.task.fishmarket.utils.JwtUtils;

import java.util.List;

/**
 * @author Nikolay Boyko
 */

@Configuration
public class AppConfig {

    @Bean
    public JwtUtils jwtUtils() {
        return new JwtUtils();
    }

//    @Bean
//    public AuthenticationProvider authenticationProvider(PasswordEncoder bCryptPasswordEncoder, UserDetailsService userService) {
//        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
//        authenticationProvider.setUserDetailsService(userService);
//        authenticationProvider.setPasswordEncoder(bCryptPasswordEncoder);
//        return authenticationProvider;
//    }
//
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
//        return configuration.getAuthenticationManager();
//    }

//    @Bean
//    public LocaleResolver localeResolver() {
//        SessionLocaleResolver slr = new SessionLocaleResolver();
//        slr.setDefaultLocale(Locale.US);
//        return slr;
//    }
//
//    //TODO add language interceptor, but I think for this kind of task it's not needed
//
//    @Bean
//    public LocaleChangeInterceptor localeChangeInterceptor() {
//        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
//        lci.setParamName("lang");
//        return lci;
//    }
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(localeChangeInterceptor());
//    }
}
