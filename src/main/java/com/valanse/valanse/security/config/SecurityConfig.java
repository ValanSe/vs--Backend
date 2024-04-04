package com.valanse.valanse.security.config;

import com.valanse.valanse.security.handler.CustomOauthFailureHandler;
import com.valanse.valanse.security.handler.CustomOauthSuccessHandler;
import com.valanse.valanse.security.filter.JwtExceptionFilter;
import com.valanse.valanse.security.service.CustomOAuth2UserService;
import com.valanse.valanse.security.filter.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@Slf4j
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomOauthSuccessHandler customOauthSuccessHandler;
    private final CustomOauthFailureHandler customOauthFailureHandler;
    private final JwtAuthFilter jwtAuthFilter;
    private final JwtExceptionFilter jwtExceptionFilter;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .csrf(AbstractHttpConfigurer::disable) // csrf 비활성화 -> cookie 를 사용하지 않으면 꺼도 된다. (cookie 를 사용할 경우 httpOnly(XSS 방어), sameSite(CSRF 방어)로 방어해야 한다.)
//                .cors(AbstractHttpConfigurer::disable) // cors 비활성화 -> 프론트와 연결 시 따로 설정 필요
//                .httpBasic(AbstractHttpConfigurer::disable) // 기본 인증 로그인 비활성화
//                .formLogin(AbstractHttpConfigurer::disable) // 기본 login form 비활성화
//                .logout(AbstractHttpConfigurer::disable) // 기본 logout 비활성화
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 사용하지 않음

                // request 인증, 인가 설정
                .authorizeHttpRequests(request ->
                        request
                                .requestMatchers("/token/**", "/login", "/failure").permitAll()
                                .anyRequest().authenticated()
                )

                .oauth2Login(oauth2 ->
                        oauth2
                                .userInfoEndpoint(userInfoEndpointConfig ->
                                        userInfoEndpointConfig.userService(customOAuth2UserService))
                                .failureHandler(customOauthFailureHandler)
                                .successHandler(customOauthSuccessHandler)
                );


        // JWT 인증 필터를 UsernamePasswordAuthenticationFilter 앞에 추가한다.
        return httpSecurity
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtExceptionFilter, JwtAuthFilter.class)
                .build();
    }

}
