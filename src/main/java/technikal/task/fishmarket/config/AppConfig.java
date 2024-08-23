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
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import technikal.task.fishmarket.config.interceptor.JwtAuthorizationFilter;
import technikal.task.fishmarket.utils.JwtUtils;

/**
 * @author Nikolay Boyko
 */

@Configuration
public class AppConfig implements WebMvcConfigurer {

    @Bean
    public JwtUtils jwtUtils() {
        return new JwtUtils();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(@Lazy UserDetailsService userDetailsService){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(@Lazy AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter(@Lazy UserDetailsService userService, JwtUtils jwtUtils) {
        return new JwtAuthorizationFilter(jwtUtils, userService);
    }

//    @Bean
//    public FilterRegistrationBean<JwtAuthenticationFilter> jwtAuthenticationFilterRegistrationBean(JwtAuthenticationFilter filter) {
//        FilterRegistrationBean<JwtAuthenticationFilter> frb = new FilterRegistrationBean<>(filter);
//        frb.setEnabled(false);
//        return frb;
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
