package com.valanse.valanse.security.service;

import com.valanse.valanse.entity.NaverUser;
import com.valanse.valanse.entity.User;
import com.valanse.valanse.exception.CustomOAuth2AuthenticationException;
import com.valanse.valanse.repository.jpa.NaverUserRepository;
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
public class NaverOAuth2UserInfoService implements OAuth2UserInfoService {

    private final NaverUserRepository naverUserRepository;
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
    public boolean isUserRegistered(OAuth2User oAuth2User) throws Exception {
        try {
            Map<String, Object> response = getResponse(oAuth2User);

            String naverUserId = (String) response.get("id");

            Optional<NaverUser> naverUser = naverUserRepository.findById(naverUserId);

            if (naverUser.isPresent()) {
                // 해당 유저가 존재할 경우, true 반환
                return true;
            } else {
                // 유저가 존재하지 않을 경우, 회원가입 로직 실행 후 false 반환
                return false;
            }
        } catch (Exception e) {
            log.error("Failed to check User is registered {}: {}", oAuth2User, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void registerUser(OAuth2User oAuth2User, String registrationId) throws Exception {
        try {
            Map<String, Object> response = getResponse(oAuth2User);

            // response Map에서 필요한 정보 추출
            String id = (String) response.get("id");
            String email = (String) response.get("email");
            String name = (String) response.get("name");

            // 추출한 정보를 사용해 NaverUser 객체 생성 및 저장
            NaverUser naverUser = NaverUser.builder()
                    .id(id)
                    .email(email)
                    .name(name)
                    .build();

            naverUserRepository.save(naverUser);

            User user = User.builder()
                    .oauthProvider("naver") // 이전 코드에서 naverUserId 변수가 정의되지 않았으므로, id를 사용
                    .oauthId(id)
                    .status("normal")
                    .role("user")
                    .createdAt(LocalDateTime.now())
                    .build();

            userRepository.save(user);

            // 기본 아이템 세팅
        } catch (Exception e) {
            log.error("Error registering user with oAuth2User data {}: {}", oAuth2User, e.getMessage(), e);
            throw e;
        }
    }

    private static Map<String, Object> getResponse(OAuth2User oAuth2User) throws Exception {
        try {
            Map<String, Object> attributes = oAuth2User.getAttributes();

            // 'response' 내부의 Map을 추출
            Object response = attributes.get("response");
            if (response == null) {
                log.error("User attributes did not contain 'response' key.");
                throw new Exception("User attributes did not contain 'response' key.");
            }
            return (Map<String, Object>) response;

        } catch (Exception e) {
            log.error("An error occurred getting response from OAuth2User: {}", e.getMessage(), e);
            throw e;
        }

    }

}
