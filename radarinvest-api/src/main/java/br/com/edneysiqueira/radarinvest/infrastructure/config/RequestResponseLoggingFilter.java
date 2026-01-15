package br.com.edneysiqueira.radarinvest.infrastructure.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class RequestResponseLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestURI = request.getRequestURI();

        // Exclude requests to Swagger do log
        if (!requestURI.startsWith("/radarinvest") ||
                requestURI.contains("/docs") ||
                requestURI.contains("/api-docs") ||
                requestURI.contains("/swagger-ui") ||
                requestURI.contains("/favicon")) {
            filterChain.doFilter(request, response);
            return;
        }

        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        try {
            filterChain.doFilter(requestWrapper, responseWrapper);
        } finally {
            this.logRequest(requestWrapper);
            this.logResponse(responseWrapper);
            responseWrapper.copyBodyToResponse();
        }
    }

    private void logRequest(ContentCachingRequestWrapper request) {
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String body = new String(request.getContentAsByteArray(), StandardCharsets.UTF_8);

        log.info("REQUEST [{} {}] Payload: {}", method, uri, body.replaceAll("\n", ""));
    }

    private void logResponse(ContentCachingResponseWrapper response) {
        int status = response.getStatus();
        String body = new String(response.getContentAsByteArray(), StandardCharsets.UTF_8);

        log.info("RESPONSE [{}] Payload: {}", status, body);
    }
}
