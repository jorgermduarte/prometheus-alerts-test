package pt.jorgeduarte.javaappservlet;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Configuration
public class CustomPrometheusFilterConfig {
    private static final Logger logger = LoggerFactory.getLogger(CustomPrometheusFilterConfig.class);
    @Bean
    public FilterRegistrationBean<CustomTaggingFilter> customServletTaggingFilter(MeterRegistry meterRegistry) {
        FilterRegistrationBean<CustomTaggingFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new CustomTaggingFilter(meterRegistry));
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }

    @WebFilter("/*")
    @Order(0)
    public static class CustomTaggingFilter extends OncePerRequestFilter {
        private final MeterRegistry meterRegistry;

        public CustomTaggingFilter(MeterRegistry meterRegistry) {
            this.meterRegistry = meterRegistry;
        }

        @Override
        protected void doFilterInternal(
                HttpServletRequest request,
                HttpServletResponse response,
                FilterChain filterChain) throws ServletException, IOException {

            long startTime = System.currentTimeMillis();
            String uri = request.getRequestURI();

            int statusCode = 0;
            String exceptionName = "none";
            String error = "none";
            String outcome = "SUCCESS";

            try {
                filterChain.doFilter(request, response);
                statusCode = response.getStatus();
            } catch (Exception ex) {
                exceptionName = ex.getClass().getSimpleName();
                error = ex.getMessage();
                statusCode = response.getStatus();

                filterChain.doFilter(request,response);
            } finally {
                if (statusCode != 0) {
                    if (statusCode >= 500 && statusCode <= 599) {
                        outcome = "SERVER_ERROR";
                    }else if(statusCode >= 400 && statusCode <= 499){
                        outcome = "CLIENT_ERROR";
                    }

                    // Since the status code is only changed on the doFilter afterward we are going to force the status code as internal server error for prometheus
                    // when an exception has been detected
                    if(!exceptionName.equals("none") && statusCode >= 100 && statusCode < 399){
                            statusCode = 500;
                    }

                    Timer.builder("http.server.requests")
                            .tags("uri", uri, "status", String.valueOf(statusCode), "exception", exceptionName, "error", error, "outcome", outcome)
                            .register(meterRegistry)
                            .record(System.currentTimeMillis() - startTime, TimeUnit.MILLISECONDS);
                }
            }
        }
    }

}
