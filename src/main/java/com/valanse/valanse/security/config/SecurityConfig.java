package com.valanse.valanse.security.config;

import com.valanse.valanse.security.filter.JwtAuthFilter;
import com.valanse.valanse.security.filter.JwtExceptionFilter;
import com.valanse.valanse.security.handler.CustomOauthFailureHandler;
import com.valanse.valanse.security.handler.CustomOauthSuccessHandler;
import com.valanse.valanse.security.service.CustomOAuth2UserService;
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
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception { // 보안 구성 설정; 요청이 서버로 들어오면 필터 체인을 통해 요청이 처리됨

        httpSecurity.csrf(AbstractHttpConfigurer::disable); // csrf 비활성화

        httpSecurity.httpBasic(AbstractHttpConfigurer::disable) // 기본 인증 로그인 비활성화
                .formLogin(AbstractHttpConfigurer::disable) // 기본 login form 비활성화
                .logout(AbstractHttpConfigurer::disable) // 기본 logout 비활성화
                .sessionManagement(sessionManagement -> // 세션 관리 정책 설정
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 사용하지 않음

                // request 인증, 인가 설정
                .authorizeHttpRequests(request ->
                        request
                                .requestMatchers("/token/**", "/login","/v3/api-docs/**", "/swagger/**", "/swagger-ui.html", "/swagger-ui/**").permitAll()
                                .anyRequest().authenticated()
                )

                // OAuth2 로그인 설정
                .oauth2Login(oauth2 ->
                        oauth2
                                .userInfoEndpoint(userInfoEndpointConfig -> // 사용자 정보 엔드포인트 설정; 엔드포인트: OAuth2 로그인 시 사용자의 프로필 정보를 가져오는 역할
                                        userInfoEndpointConfig.userService(customOAuth2UserService)) // userService를 통한 사용자 서비스 구성
                                .failureHandler(customOauthFailureHandler)
                                .successHandler(customOauthSuccessHandler) // 로그인 성공 및 실패 핸들러
                );


        // JWT 인증 필터를 UsernamePasswordAuthenticationFilter 앞에 추가한다. JWT 인증 필터가 인증 요청 처리
        return httpSecurity
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtExceptionFilter, JwtAuthFilter.class)
                .build();
    }

}
