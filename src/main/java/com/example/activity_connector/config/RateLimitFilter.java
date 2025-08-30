package com.example.activity_connector.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.time.Duration;

@Configuration
public class RateLimitFilter implements Filter {

    private final Bucket bucket;
    @Value("${rate-limiting.requests-per-minute}")
    private int requestsPerMinute;

    public RateLimitFilter() {
        Bandwidth limit = Bandwidth.simple(requestsPerMinute, Duration.ofMinutes(1));
        this.bucket = Bucket.builder()
                .addLimit(limit)
                .build();
    }
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        if (bucket.tryConsume(1)) {
            try {
                filterChain.doFilter(servletRequest, servletResponse);
            } catch (Exception e) {
                httpResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } else {
            httpResponse.setStatus(429);
            httpResponse.getWriter().write("Rate limit exceeded. Try again later.");
        }
    }
}
