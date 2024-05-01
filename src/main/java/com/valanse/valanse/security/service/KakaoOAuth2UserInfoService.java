package com.valanse.valanse.security.service;


import com.valanse.valanse.entity.KakaoUser;
import com.valanse.valanse.entity.User;
import com.valanse.valanse.exception.CustomOAuth2AuthenticationException;
import com.valanse.valanse.repository.jpa.KakaoUserRepository;
import com.valanse.valanse.repository.jpa.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class KakaoOAuth2UserInfoService implements OAuth2UserInfoService {

    private final KakaoUserRepository kakaoUserRepository;
    private final UserRepository userRepository;

    @Override
    public OAuth2User processOAuth2User(String oauthProvider, OAuth2User oAuth2User) {
        try {
            if (isUserRegistered(oAuth2User)) {

            } else {
                registerUser(oAuth2User, oauthProvider);
            }

        } catch (Exception e) {
            throw new CustomOAuth2AuthenticationException("An error occurred processing OAuth2User", e);
        }

        return oAuth2User;

    }

    @Override
    public boolean isUserRegistered(OAuth2User oAuth2User) throws Exception{
        try {
            Map<String, Object> attributes = oAuth2User.getAttributes();

            String id = attributes.get("id").toString();

            Optional<KakaoUser> kakaoUser = kakaoUserRepository.findById(id);

            if (kakaoUser.isPresent()) {
                // 해당 유저가 존재할 경우
                return true;
            } else {
                // 유저가 존재하지 않을 경우
                return false;
            }
        } catch (Exception e) {
            log.error("Failed to check User is registered {}: {}", oAuth2User, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void registerUser(OAuth2User oAuth2User, String registrationId) {
        try {
            Map<String, Object> attributes = oAuth2User.getAttributes();

            String id = attributes.get("id").toString();
            Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
            String nickname = properties.get("nickname").toString();

            // 추출한 정보를 사용해 NaverUser 객체 생성 및 저장
            KakaoUser kakaoUser = KakaoUser.builder()
                    .id(id)
                    .name(nickname)
                    .build();

            kakaoUserRepository.save(kakaoUser);

            User user = User.builder()
                    .oauthProvider("kakao")
                    .oauthId(id)
                    .status("normal")
                    .role("user")
                    .createdAt(LocalDateTime.now())
                    .build();

            userRepository.save(user);

        } catch (Exception e) {
            log.error("Error registering user with response data {}: {}", oAuth2User, e.getMessage(), e);
            throw e;
        }
    }
}
