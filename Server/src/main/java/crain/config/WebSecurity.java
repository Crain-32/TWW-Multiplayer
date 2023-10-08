package crain.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurity {

    /**
     * Really this should be removed, but I'm lazy, and eventually plan on requiring some authentication on some
     * Admin related things, for now I just use the fact that the Port is hardlocked to my IP address.
     * Should I state that in code? Idc, it's probably on the Discord anyway
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((requests) -> requests
                .requestMatchers("/app/**", "/topic/**", "/rest/**", "/swagger-ui/**", "/favicon.ico", "/index.html", "/v3/**")
                .permitAll().anyRequest().permitAll());
        http.csrf(Customizer.withDefaults());
        return http.build();
    }
}
