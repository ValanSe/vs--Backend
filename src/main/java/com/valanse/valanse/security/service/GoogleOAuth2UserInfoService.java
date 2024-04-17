package com.valanse.valanse.security.service;

import com.valanse.valanse.entity.GoogleUser;
import com.valanse.valanse.entity.User;
import com.valanse.valanse.exception.CustomOAuth2AuthenticationException;
import com.valanse.valanse.repository.GoogleUserRepository;
import com.valanse.valanse.repository.UserRepository;
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
public class GoogleOAuth2UserInfoService implements OAuth2UserInfoService{

    private final GoogleUserRepository googleUserRepository;
    private final UserRepository userRepository;

    // OAuth2 사용자 정보를 처리하고 이미 등록된 사용자인지 확인
    @Override
    public OAuth2User processOAuth2User(String oauthProvider, OAuth2User oAuth2User) {
        try {
            if (isUserRegistered(oAuth2User)) {

            }else {
                registerUser(oAuth2User, oauthProvider);
            }

        } catch (Exception e) {
            throw new CustomOAuth2AuthenticationException("An error occurred processing OAuth2User", e);
        }

        return oAuth2User;
    }

    // 주어진 OAuth2 사용자가 이미 데이터베이스에 등록되어 있는지 확인
    @Override
    public boolean isUserRegistered(OAuth2User oAuth2User) {
        try {
            Map<String, Object> attributes = oAuth2User.getAttributes();
            String id = (String) attributes.get("sub"); // 사용자 고유 ID

            Optional<GoogleUser> googleUser = googleUserRepository.findById(id); // 데이터베이스에서 사용자를 찾음

            if (googleUser.isPresent()) {
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

    // OAuth2로부터 받은 사용자 정보를 사용하여 새로운 사용자 등록
    @Override
    public void registerUser(OAuth2User oAuth2User, String registrationId) {
        try {
            Map<String, Object> attributes = oAuth2User.getAttributes();

            String id = (String) attributes.get("sub");
            String email = (String) attributes.get("email");
            String name = (String) attributes.get("name");

            // 추출한 정보를 사용해 GoogleUser 객체 생성 및 저장
            GoogleUser googleUser = GoogleUser.builder()
                    .id(id)
                    .email(email)
                    .name(name)
                    .build();

            googleUserRepository.save(googleUser); // 데이터베이스에 저장

            User user = User.builder()
                    .oauthProvider("google")
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
