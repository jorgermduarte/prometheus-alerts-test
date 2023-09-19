package pt.jorgeduarte.javaappservlet.Controllers;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Configuration
public class PrometheusUriFilterConfig {
    @Bean
    public FilterRegistrationBean<UriTaggingFilter> uriTaggingFilter(MeterRegistry meterRegistry) {
        FilterRegistrationBean<UriTaggingFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new UriTaggingFilter(meterRegistry));
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }

    @WebFilter("/*")
    public static class UriTaggingFilter extends OncePerRequestFilter {
        private final MeterRegistry meterRegistry;

        public UriTaggingFilter(MeterRegistry meterRegistry) {
            this.meterRegistry = meterRegistry;
        }

        @Override
        protected void doFilterInternal(
                HttpServletRequest request,
                HttpServletResponse response,
                FilterChain filterChain) throws ServletException, IOException {

            long startTime = System.currentTimeMillis();

            try {
                filterChain.doFilter(request, response);
            } finally {
                long endTime = System.currentTimeMillis();
                long requestTime = endTime - startTime;

                String uri = request.getRequestURI();

                Timer.builder("http.server.requests")
                        .tags("uri", uri)
                        .register(meterRegistry)
                        .record(requestTime, TimeUnit.MILLISECONDS);
            }
        }
    }
}