package com.valanse.valanse.controller;

import com.valanse.valanse.dto.StatusResponseDto;
import com.valanse.valanse.dto.response.TokenResponseStatus;
import com.valanse.valanse.redis.repository.RefreshTokenRepository;
import com.valanse.valanse.redis.entity.RefreshToken;
import com.valanse.valanse.redis.service.TokenService;
import com.valanse.valanse.security.util.JwtUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenService tokenService;
    private final JwtUtil jwtUtil;


    @PostMapping("token/get")
    public ResponseEntity<StatusResponseDto> getAccessToken(@RequestHeader("stateToken") final String stateToken) {
        return ResponseEntity.ok(StatusResponseDto.success(jwtUtil.getAccessTokenByStateToken(stateToken)));
    }

    @PostMapping("token/check/expiration")
    public ResponseEntity<StatusResponseDto> check1(@RequestHeader("Authorization") final String accessToken) {

        long remainingExpirationTimeInMinutes = jwtUtil.getRemainingExpirationTimeInMinutes(accessToken);
        return ResponseEntity.ok(StatusResponseDto.success(remainingExpirationTimeInMinutes));
    }


    @PostMapping("token/check/allClaimsFromToken")
    public ResponseEntity<StatusResponseDto> check2(@RequestHeader("Authorization") final String accessToken) {

        Map<String, Object> allClaimsFromToken = jwtUtil.getAllClaimsFromToken(accessToken);
        return ResponseEntity.ok(StatusResponseDto.success(allClaimsFromToken));
    }

    @PostMapping("token/logout")
    public ResponseEntity<StatusResponseDto> logout(@RequestHeader("Authorization") final String accessToken) {

        // 엑세스 토큰으로 현재 Redis 정보 삭제
        tokenService.removeRefreshToken(accessToken);
        return ResponseEntity.ok(StatusResponseDto.success());
    }

    @PostMapping("/token/refresh")
    public ResponseEntity<TokenResponseStatus> refresh(@RequestHeader("Authorization") final String accessToken) {
        RefreshToken refreshToken = refreshTokenRepository.findByAccessToken(accessToken)
                .orElseThrow(EntityNotFoundException::new);
        // RefreshToken이 존재하고 유효하다면 실행
        if (jwtUtil.verifyToken(refreshToken.getRefreshToken())) {
            // 권한과 아이디를 추출해 새로운 액세스토큰을 만든다.
            String newAccessToken = jwtUtil.generateAccessToken(refreshToken.getUserIdx(), jwtUtil.getUserRole(refreshToken.getRefreshToken()));
            // 액세스 토큰의 값을 수정해준다.
            refreshToken.updateAccessToken(newAccessToken);
            refreshTokenRepository.save(refreshToken);
            // 새로운 액세스 토큰을 반환해준다.
            return ResponseEntity.ok(TokenResponseStatus.success(newAccessToken));
        }

        // 리프레쉬 토큰이 존재하지 않거나 유효하지 않은 토큰이라면 빈 토큰을 보냄
        return ResponseEntity.badRequest().body(TokenResponseStatus.addStatus(400, null));
    }

}
