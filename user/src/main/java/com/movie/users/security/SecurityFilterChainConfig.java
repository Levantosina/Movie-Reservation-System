package com.movie.users.security;



import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author DMITRII LEVKIN on 10/10/2024
 * @project MovieReservationSystem
 */
@Configuration
@EnableWebSecurity
public class SecurityFilterChainConfig {
    private final AuthenticationProvider authenticationProvider;
    private final UserJWTAuthenticationFilter userJwtAuthenticationFilter;


    private  final AuthenticationEntryPoint authenticationEntryPoint;


    public SecurityFilterChainConfig(AuthenticationProvider authenticationProvider,UserJWTAuthenticationFilter userJwtAuthenticationFilter, AuthenticationEntryPoint authenticationEntryPoint) {
        this.authenticationProvider = authenticationProvider;
        this.userJwtAuthenticationFilter = userJwtAuthenticationFilter;

        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests()
                .requestMatchers(HttpMethod.POST, "/api/v1/users/**", "/api/v1/auth/login",
                        "/api/v1/admin/reset-password").permitAll()
                .requestMatchers(HttpMethod.GET,"/api/v1/users/**").authenticated()
                .requestMatchers(HttpMethod.DELETE,"/api/v1/users/**").authenticated()
                .requestMatchers(HttpMethod.PUT,"/api/v1/users/**").authenticated()
                .requestMatchers(HttpMethod.POST,"api/v1/admin/**").hasRole("ADMIN")
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(userJwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint);

        return http.build();
    }
}