package technikal.task.fishmarket.imports;

import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.TestingAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import technikal.task.fishmarket.config.SecurityConfig;
import technikal.task.fishmarket.exception.FilterExceptionHandler;
import technikal.task.fishmarket.repository.FishRepository;
import technikal.task.fishmarket.services.FishService;
import technikal.task.fishmarket.services.impl.UserDetailsServiceImpl;
import technikal.task.fishmarket.utils.JwtUtils;

import java.lang.annotation.*;

/**
 * @author Nikolay Boyko
 */

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({FishRepository.class, FishService.class, UserDetailsServiceImpl.class, SecurityConfig.class, JwtUtils.class, FilterExceptionHandler.class, TestingAuthenticationProvider.class})
public @interface ClassesImports {
}