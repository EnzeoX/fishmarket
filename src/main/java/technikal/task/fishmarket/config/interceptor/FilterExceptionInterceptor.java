package technikal.task.fishmarket.config.interceptor;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import technikal.task.fishmarket.exception.FilterExceptionHandler;

import java.io.IOException;

/**
 * @author Nikolay Boyko
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class FilterExceptionInterceptor extends OncePerRequestFilter {

    private final FilterExceptionHandler filterExceptionHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error("Error occurred while processing filter chain. Message: {}", e.getMessage());
            filterExceptionHandler.handleError(request, response, e);
        }
    }
}
