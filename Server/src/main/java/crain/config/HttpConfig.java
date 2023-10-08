package crain.config;

import crain.util.filter.AdminFilter;
import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Objects;

@Configuration
@EnableWebMvc
public class HttpConfig implements WebMvcConfigurer {

    @Value("${admin.port}")
    private Integer adminPort;
    @Value("${server.port}")
    private int serverPort;

    @Bean
    public ServletWebServerFactory servletWebServerFactory() {
        var tomcatServletWebServerFactory = new TomcatServletWebServerFactory();
        tomcatServletWebServerFactory.addAdditionalTomcatConnectors(createStandardConnector());
        return tomcatServletWebServerFactory;
    }
    private Connector createStandardConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setPort(Objects.requireNonNullElseGet(adminPort, () -> serverPort));
        return connector;
    }

    @Configuration
    @ConditionalOnProperty(name = "enable.admin.controller", havingValue = "true")
    @ServletComponentScan(basePackageClasses = AdminFilter.class)
    static class AdminFilterRegistration {
        // Registers the Admin Filter only if we enable the Admin Controller
    }
}
