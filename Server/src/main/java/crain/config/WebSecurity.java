package crain.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurity {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((requests) -> requests
                .requestMatchers("/app/**", "/topic/**", "/rest/**", "/swagger-ui/**", "/favicon.ico", "/index.html", "/v3/**")
                .permitAll().anyRequest().permitAll());
        http.csrf().disable();
        return http.build();
    }
}
