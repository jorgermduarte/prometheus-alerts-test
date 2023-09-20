package pt.jorgeduarte.javaappservlet.Servlets;

import jakarta.servlet.Servlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomServletConfig {
    @Bean
    public ServletRegistrationBean<Servlet> customServlet() {
        ServletRegistrationBean<Servlet> registrationBean =
                new ServletRegistrationBean<>(new CustomServlet(), "/myservlet/*");

        return registrationBean;
    }
}