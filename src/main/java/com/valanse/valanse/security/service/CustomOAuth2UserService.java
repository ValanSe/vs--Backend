package com.valanse.valanse.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final GoogleOAuth2UserInfoService googleOAuth2UserInfoService;
    private final NaverOAuth2UserInfoService naverOAuth2UserInfoService;
    private final KakaoOAuth2UserInfoService kakaoOAuth2UserInfoService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        try {
            // 기본 OAuth2UserService 객체 생성
            OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();

            // OAuth2UserService를 사용하여 OAuth2User 정보를 가져온다.
            OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

            String registrationId = userRequest.getClientRegistration().getRegistrationId();

            switch (registrationId) {
                case "google":
                    return googleOAuth2UserInfoService.processOAuth2User(registrationId, oAuth2User);
                case "naver":
                    return naverOAuth2UserInfoService.processOAuth2User(registrationId, oAuth2User);
                case "kakao":
                    return kakaoOAuth2UserInfoService.processOAuth2User(registrationId, oAuth2User);
                default:
                    throw new OAuth2AuthenticationException("Unsupported provider: " + registrationId);
            }
        } catch (OAuth2AuthenticationException e) {
            log.error("Error processing OAuth2 user from provider '{}': {}", userRequest.getClientRegistration().getRegistrationId(), e.toString());
            throw e;
        }
    }
}

