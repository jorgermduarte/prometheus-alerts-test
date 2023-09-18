package pt.jorgeduarte.javaappservlet;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.PropertySource;

@AutoConfiguration
@PropertySource("classpath:endpoints.properties")
public class EndpointsPropertiesAutoConfiguration {
}
