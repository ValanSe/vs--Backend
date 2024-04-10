package com.valanse.valanse.security.handler;

import com.valanse.valanse.entity.User;
import com.valanse.valanse.repository.jpa.GoogleUserRepository;
import com.valanse.valanse.repository.jpa.KakaoUserRepository;
import com.valanse.valanse.repository.jpa.NaverUserRepository;
import com.valanse.valanse.repository.jpa.UserRepository;
import com.valanse.valanse.security.dto.GeneratedToken;
import com.valanse.valanse.security.util.JwtUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomOauthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final GoogleUserRepository googleUserRepository;
    private final NaverUserRepository naverUserRepository;
    private final KakaoUserRepository kakaoUserRepository;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        if (authentication instanceof OAuth2AuthenticationToken oauthToken) {

            // 클라이언트 등록 ID(즉, 프로바이더 식별자)를 얻습니다.
            String registrationId = oauthToken.getAuthorizedClientRegistrationId();

            // OAuth2User로 캐스팅하여 인증된 사용자 정보를 가져옵니다.
            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

            // 프로바이더별 사용자 정보 처리
            User user = processUserInformation(registrationId, oAuth2User);

            // JWT 토큰을 생성하고 HTTP 응답 헤더에 포함시켜 클라이언트에 전달
            GeneratedToken generatedToken = jwtUtil.generateToken(user.getUserId(), user.getRole());
            log.info("accessToken = {}", generatedToken.getAccessToken());
            log.info("refreshToken = {}", generatedToken.getRefreshToken());

            httpServletResponse.setHeader("Authorization", "Bearer " + generatedToken.getAccessToken());

            // 사용자를 리디렉션할 URL 설정
            httpServletResponse.sendRedirect("/success");

        }
    }

    // OAuth2 사용자의 속성을 가져옴
    private User processUserInformation(String registrationId, OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String id; // 사용자 고유 식별 값

        // 등록된 제공자별로 사용자 고유 식별 값 추출
        switch (registrationId) {
            case "google":
                id = (String) attributes.get("sub"); // 서브 식별자 추출
                return findUserByProviderId(googleUserRepository, id, registrationId); // 사용자 정보를 데이터베이스에서 찾음

            case "naver":
                Map<String, Object> response = (Map<String, Object>) attributes.get("response"); // 제공자의 사용자 정보를 포함한 응답에서 ID 추출
                id = (String) response.get("id");
                return findUserByProviderId(naverUserRepository, id, registrationId);

            case "kakao":
                id = attributes.get("id").toString(); // kakao 제공자의 ID 추출
                return findUserByProviderId(kakaoUserRepository, id, registrationId);

            default:
                throw new OAuth2AuthenticationException("Unsupported provider: " + registrationId);
        }
    }

    // 제공된 Repository에서 특정 ID를 사용하여 사용자를 검색
    private <T> User findUserByProviderId(JpaRepository<T, String> repository, String id, String registrationId) {
        T providerUser = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found in OauthProviderUserRepository" + " : " + registrationId + " -> " + id));

        User user = userRepository.findByOauthId(id).orElseThrow(() -> new EntityNotFoundException("User not found in UserRepository" + " : " + id));
        return user;
    }
}
