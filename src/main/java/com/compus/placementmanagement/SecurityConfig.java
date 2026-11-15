
package com.compus.placementmanagement;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration =
                new CorsConfiguration();

        // Allow local frontend and deployed Netlify frontend
        configuration.setAllowedOrigins(
                List.of(
                        "http://localhost:5173",
                        "https://campus-placement-management.netlify.app"
                )
        );

        configuration.setAllowedMethods(
                List.of(
                        "GET",
                        "POST",
                        "PUT",
                        "DELETE",
                        "OPTIONS"
                )
        );

        configuration.setAllowedHeaders(
                List.of("*")
        );

        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration(
                "/**",
                configuration
        );

        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
                .cors(cors -> {})

                .csrf(csrf -> csrf.disable())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                .authorizeHttpRequests(auth -> {

                    // PUBLIC ENDPOINTS

                    auth.requestMatchers(
                            "/auth/login",
                            "/student-auth/login",
                            "/admin-auth/login",
                            "/admin/create"
                    ).permitAll();

                    auth.requestMatchers(
                            HttpMethod.POST,
                            "/students/register"
                    ).permitAll();

                    auth.requestMatchers(
                            HttpMethod.POST,
                            "/companies/register"
                    ).permitAll();

                    // ADMIN ENDPOINTS

                    auth.requestMatchers(
                            HttpMethod.GET,
                            "/admin/**"
                    ).hasRole("ADMIN");

                    auth.requestMatchers(
                            HttpMethod.POST,
                            "/admin/**"
                    ).hasRole("ADMIN");

                    auth.requestMatchers(
                            HttpMethod.PUT,
                            "/admin/**"
                    ).hasRole("ADMIN");

                    auth.requestMatchers(
                            HttpMethod.DELETE,
                            "/admin/**"
                    ).hasRole("ADMIN");

                    // STUDENT

                    auth.requestMatchers(
                            "/students/**"
                    ).hasRole("STUDENT");

                    auth.requestMatchers(
                            "/eligibility/**"
                    ).hasRole("STUDENT");

                    auth.requestMatchers(
                            HttpMethod.POST,
                            "/resume/upload/**"
                    ).hasRole("STUDENT");

                    // RESUME VIEW

                    auth.requestMatchers(
                            HttpMethod.GET,
                            "/resume/view/**"
                    ).hasRole("COMPANY");

                    // JOBS

                    auth.requestMatchers(
                            HttpMethod.GET,
                            "/jobs"
                    ).hasAnyRole(
                            "STUDENT",
                            "COMPANY",
                            "ADMIN"
                    );

                    auth.requestMatchers(
                            HttpMethod.GET,
                            "/jobs/*"
                    ).hasAnyRole(
                            "STUDENT",
                            "COMPANY",
                            "ADMIN"
                    );

                    auth.requestMatchers(
                            HttpMethod.GET,
                            "/jobs/company/**"
                    ).hasRole("COMPANY");

                    auth.requestMatchers(
                            HttpMethod.POST,
                            "/jobs"
                    ).hasRole("COMPANY");

                    auth.requestMatchers(
                            HttpMethod.PUT,
                            "/jobs/**"
                    ).hasRole("COMPANY");

                    auth.requestMatchers(
                            HttpMethod.DELETE,
                            "/jobs/**"
                    ).hasRole("COMPANY");

                    // APPLICATIONS

                    auth.requestMatchers(
                            HttpMethod.POST,
                            "/applications"
                    ).hasRole("STUDENT");

                    auth.requestMatchers(
                            HttpMethod.GET,
                            "/applications/student/**"
                    ).hasRole("STUDENT");

                    auth.requestMatchers(
                            HttpMethod.GET,
                            "/applications/job/**"
                    ).hasRole("COMPANY");

                    auth.requestMatchers(
                            HttpMethod.PUT,
                            "/applications/**"
                    ).hasRole("COMPANY");

                    auth.requestMatchers(
                            HttpMethod.GET,
                            "/applications"
                    ).hasRole("ADMIN");

                    auth.requestMatchers(
                            HttpMethod.GET,
                            "/applications/*"
                    ).hasRole("ADMIN");

                    auth.requestMatchers(
                            HttpMethod.DELETE,
                            "/applications/**"
                    ).hasRole("ADMIN");

                    // COMPANY

                    auth.requestMatchers(
                            HttpMethod.GET,
                            "/companies/**"
                    ).hasAnyRole(
                            "COMPANY",
                            "ADMIN"
                    );

                    auth.requestMatchers(
                            HttpMethod.POST,
                            "/companies/**"
                    ).hasRole("COMPANY");

                    auth.requestMatchers(
                            HttpMethod.PUT,
                            "/companies/**"
                    ).hasRole("COMPANY");

                    auth.requestMatchers(
                            HttpMethod.DELETE,
                            "/companies/**"
                    ).hasRole("COMPANY");

                    // EVERYTHING ELSE

                    auth.anyRequest().authenticated();
                });

        http.addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class
        );

        return http.build();
    }
}
