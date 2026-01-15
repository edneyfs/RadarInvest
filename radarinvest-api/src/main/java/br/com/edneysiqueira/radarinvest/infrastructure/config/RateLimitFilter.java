package br.com.edneysiqueira.radarinvest.infrastructure.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class RateLimitFilter implements Filter {

    private final Map<String, RequestLog> keyMap = new ConcurrentHashMap<>();
    private static final long TIME_WINDOW = 1000; // 1 second
    private static final int MAX_REQUESTS = 10;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String requestURI = httpServletRequest.getRequestURI();

        // Whitelist Swagger and H2 Console
        if (requestURI.contains("/docs") ||
        // requestURI.contains("/api-docs") ||
                requestURI.contains("/swagger-ui") ||
                // requestURI.contains("/favicon") ||
                requestURI.contains("/h2-console")) {
            chain.doFilter(request, response);
            return;
        }

        String ipAddress = httpServletRequest.getRemoteAddr();

        if (isRateLimited(ipAddress)) {
            ((HttpServletResponse) response).setStatus(429); // Too Many Requests
            response.getWriter().write("Too many requests");
            return;
        }

        chain.doFilter(request, response);
    }

    private boolean isRateLimited(String ip) {
        long now = System.currentTimeMillis();
        keyMap.putIfAbsent(ip, new RequestLog(now, new AtomicInteger(0)));
        RequestLog log = keyMap.get(ip);

        synchronized (log) {
            if (now - log.startTime > TIME_WINDOW) {
                log.startTime = now;
                log.count.set(1);
                return false;
            } else {
                return log.count.incrementAndGet() > MAX_REQUESTS;
            }
        }
    }

    private static class RequestLog {
        long startTime;
        AtomicInteger count;

        RequestLog(long startTime, AtomicInteger count) {
            this.startTime = startTime;
            this.count = count;
        }
    }
}
