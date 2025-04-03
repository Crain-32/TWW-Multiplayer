package crain.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

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
                        .requestMatchers("/app/**",
                                "/topic/.",
                                "/rest/**",
                                "/swagger-ui/**",
                                "/favicon.ico",
                                "/index.html",
                                "/v3/**")
                        .permitAll()
                        .requestMatchers(
                                new AntPathRequestMatcher("/admin"),
                                new AntPathRequestMatcher("/admin/**")
                        ).authenticated()
                        .anyRequest().permitAll())
                .csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(@Value("${admin.user.password}") String adminPassword) {
        UserDetails crain = User.withUsername("admin")
                .password(adminPassword)
                .build();
        return new InMemoryUserDetailsManager(crain);

    }
}
