package com.aniket.TicketBay.config;

import com.aniket.TicketBay.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    @Primary
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth-> auth
                        .requestMatchers(HttpMethod.GET,"/events","/api/events/test","/events/**").permitAll()
                        .anyRequest().authenticated()

                )
                .oauth2Login(
                        oauth2->oauth2
                                .userInfoEndpoint(userInfo->userInfo
                                        .oidcUserService(customOAuth2UserService))
                                .successHandler(successHandler())
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/events")    // redirect here after logout
                        .deleteCookies("JSESSIONID")
                        .invalidateHttpSession(true)
                );
        return http.build();
    }
    @Bean
    public AuthenticationSuccessHandler successHandler() {
        // After a successful login, send the user to /my-bookings
        return (request, response, authentication) ->
                response.sendRedirect("/my-bookings");
    }
}
