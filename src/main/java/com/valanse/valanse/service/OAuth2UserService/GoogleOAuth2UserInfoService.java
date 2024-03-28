package com.valanse.valanse.service.OAuth2UserService;

import com.valanse.valanse.entity.GoogleUser;
import com.valanse.valanse.entity.User;
import com.valanse.valanse.exception.CustomOAuth2AuthenticationException;
import com.valanse.valanse.repository.jpa.GoogleUserRepository;
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
public class GoogleOAuth2UserInfoService implements OAuth2UserInfoService{

    private final GoogleUserRepository googleUserRepository;
    private final UserRepository userRepository;

    @Override
    public OAuth2User processOAuth2User(String oauthProvider, OAuth2User oAuth2User) {
        try {
            if (isUserRegistered(oAuth2User)) {

            }else {
                registerUser(oAuth2User, oauthProvider);
                // 로그인 처리 (jwt)

            }

        } catch (Exception e) {
            throw new CustomOAuth2AuthenticationException("An error occurred processing OAuth2User", e);
        }

        return oAuth2User;
    }

    @Override
    public boolean isUserRegistered(OAuth2User oAuth2User) {
        try {
            Map<String, Object> attributes = oAuth2User.getAttributes();
            String id = (String) attributes.get("sub"); // 사용자 고유 ID

            Optional<GoogleUser> googleUser = googleUserRepository.findById(id);

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

    @Override
    public void registerUser(OAuth2User oAuth2User, String registrationId) {
        try {
            Map<String, Object> attributes = oAuth2User.getAttributes();

            String id = (String) attributes.get("sub");
            String email = (String) attributes.get("email");
            String name = (String) attributes.get("name");

            // 추출한 정보를 사용해 NaverUser 객체 생성 및 저장
            GoogleUser googleUser = GoogleUser.builder()
                    .id(id)
                    .email(email)
                    .name(name)
                    .build();

            googleUserRepository.save(googleUser);

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
