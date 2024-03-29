package com.valanse.valanse.controller;

import com.valanse.valanse.dto.StatusResponseDto;
import com.valanse.valanse.dto.response.TokenResponseStatus;
import com.valanse.valanse.repository.redis.RefreshTokenRepository;
import com.valanse.valanse.service.jwt.RefreshToken;
import com.valanse.valanse.service.jwt.RefreshTokenService;
import com.valanse.valanse.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenService refreshTokenService;
    private final JwtUtil jwtUtil;


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
        refreshTokenService.removeRefreshToken(accessToken);
        return ResponseEntity.ok(StatusResponseDto.success());
    }

    @PostMapping("/token/refresh")
    public ResponseEntity<TokenResponseStatus> refresh(@RequestHeader("Authorization") final String accessToken) {

        // 액세스 토큰으로 Refresh 토큰 객체를 조회
        Optional<RefreshToken> refreshTokenOptional = refreshTokenRepository.findByAccessToken(accessToken);

        // RefreshToken이 존재하고 유효하다면 실행
        if (refreshTokenOptional.isPresent() && jwtUtil.verifyToken(refreshTokenOptional.get().getRefreshToken())) {
            // RefreshToken 객체를 꺼내온다.
            RefreshToken refreshToken = refreshTokenOptional.get();
            // 권한과 아이디를 추출해 새로운 액세스토큰을 만든다.
            String newAccessToken = jwtUtil.generateAccessToken(refreshToken.getUserIdx(), jwtUtil.getRole(refreshToken.getRefreshToken()));
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
