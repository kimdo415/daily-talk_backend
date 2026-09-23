package com.karainc.dailytalk.domain.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //authenticationManager Bean 등록
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(AbstractHttpConfigurer::disable)
//                .cors(Customizer::withDefaults())
//                .sessionManagement((configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .and()
//                .authorizeRequests(authorizeRequests ->
//                        authorizeRequests
//                                .("/join", "/public/**", "/swagger-ui.html", "/login",
//                                        "/post/open/{uuid}", "/music/upload", "/music/list", "/change-accesstoken", "/post/test", "/change-refreshtoken",
//                                        "/admin/join", "/admin/login", "/report/send", "/charge", "/charge/point").permitAll()
//                                .anyRequest().authenticated()
//                )
//                .and()
//                .exceptionHandling();
//        return http.build();
//    }

//    CorsConfigurationSource corsConfigurationSource() {
//        return request -> {
//            CorsConfiguration config = new CorsConfiguration();
//            config.setAllowedHeaders(Collections.singletonList("*"));
//            config.setAllowedMethods(Collections.singletonList("*"));
//            config.setAllowedOriginPatterns(Collections.singletonList("http://localhost:8080")); // ⭐️ 허용할 origin
//            config.setAllowCredentials(true);
//            return config;
//        };
//    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(Collections.singletonList("*"));
        config.setAllowedMethods(Arrays.asList(HttpMethod.GET.name(), HttpMethod.POST.name(), HttpMethod.PUT.name(), HttpMethod.DELETE.name(), HttpMethod.OPTIONS.name()));
        config.setAllowedHeaders(Collections.singletonList("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(corsConfigurer -> corsConfigurer.configurationSource(corsConfigurationSource()))
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(authorize ->
                        authorize
                                .anyRequest().permitAll() // 모든 요청에 대해 접근을 허용

//                .authorizeHttpRequests(authorize ->
//                        authorize
//                                .requestMatchers("/member/login","/member/join").permitAll()
                );

//        http.httpBasic(HttpBasicConfigurer::disable)
//                .cors(corsConfigurer -> corsConfigurer.configurationSource(corsConfigurationSource())) // ⭐️⭐️⭐️
//                .csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(authorize ->
//                        authorize
//                                .requestMatchers("/login").permitAll()
//                );

        return http.build();
    }
}
