package com.valanse.valanse.controller;

import com.valanse.valanse.dto.StatusResponseDto;
import com.valanse.valanse.dto.response.TokenResponseStatus;
import com.valanse.valanse.redis.repository.RefreshTokenRepository;
import com.valanse.valanse.redis.entity.RefreshToken;
import com.valanse.valanse.redis.service.TokenService;
import com.valanse.valanse.security.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/token")
public class TokenController {

    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenService tokenService;
    private final JwtUtil jwtUtil;

    @GetMapping("/test")
    public ResponseEntity<StatusResponseDto> test() {
        return ResponseEntity.ok(StatusResponseDto.success("test05030924"));
    }

    @Operation(summary = "Access Token 획득", description = "상태 토큰을 기반으로 Access Token을 반환합니다.")
    @Parameter(name = "stateToken", description = "oauth 로그인 후, 얻을 수 있는 상태 토큰", required = true, example = "bc5a447a-db14-4a48-4f0e-4471712c168f")
    @PostMapping("/get")
    public ResponseEntity<StatusResponseDto> getAccessToken(@RequestHeader("stateToken") final String stateToken) {
        return ResponseEntity.ok(StatusResponseDto.success(jwtUtil.getAccessTokenByStateToken(stateToken)));
    }

    @Operation(summary = "Access Token 유료 기한 확인", description = "Access Token의 남은 유효 기간(분)을 반환합니다.")
    @Parameter(name = "Authorization", description = "jwt accessToken", required = true, example = "eyJh4444OiJIUzI1NiJ9.eyJ1c2VySWR4IjoxLCJyb2xlIjoidXNlciIsImlhdCI6MTcxNDc0Nj4444wiZXhwIjoxNzE0NzQ4NDg0fQ.gfbDplqgtNoYGI3tm4444ie2x-5VovulgjMQ8HAkYY4")
    @PostMapping("/check/expiration")
    public ResponseEntity<StatusResponseDto> check1(@RequestHeader("Authorization") final String accessToken) {

        long remainingExpirationTimeInMinutes = jwtUtil.getRemainingExpirationTimeInMinutes(accessToken);
        return ResponseEntity.ok(StatusResponseDto.success(remainingExpirationTimeInMinutes));
    }


    @Operation(summary = "Access Token 모든 클레임 조회", description = "Access Token의 모든 Claim을 반환합니다.")
    @Parameter(name = "Authorization", description = "jwt accessToken", required = true, example = "eyJh4444OiJIUzI1NiJ9.eyJ1c2VySWR4IjoxLCJyb2xlIjoidXNlciIsImlhdCI6MTcxNDc0Nj4444wiZXhwIjoxNzE0NzQ4NDg0fQ.gfbDplqgtNoYGI3tm4444ie2x-5VovulgjMQ8HAkYY4")
    @PostMapping("/check/allClaimsFromToken")
    public ResponseEntity<StatusResponseDto> check2(@RequestHeader("Authorization") final String accessToken) {

        Map<String, Object> allClaimsFromToken = jwtUtil.getAllClaimsFromToken(accessToken);
        return ResponseEntity.ok(StatusResponseDto.success(allClaimsFromToken));
    }

    @Operation(summary = "유저 로그아웃, Refresh Token 삭제", description = "Access Token과 매치되는 Refresh Token을 삭제합니다. 프론트엔드에서는 쿠키내의 Access Token을 삭제합니다.")
    @Parameter(name = "Authorization", description = "jwt accessToken", required = true, example = "eyJh4444OiJIUzI1NiJ9.eyJ1c2VySWR4IjoxLCJyb2xlIjoidXNlciIsImlhdCI6MTcxNDc0Nj4444wiZXhwIjoxNzE0NzQ4NDg0fQ.gfbDplqgtNoYGI3tm4444ie2x-5VovulgjMQ8HAkYY4")
    @PostMapping("/logout")
    public ResponseEntity<StatusResponseDto> logout(@RequestHeader("Authorization") final String accessToken) {

        // 엑세스 토큰으로 현재 Redis 정보 삭제
        tokenService.removeRefreshToken(accessToken);
        return ResponseEntity.ok(StatusResponseDto.success());
    }

    @Operation(summary = "Access Token 갱신", description = "Refresh Token을 사용하여 Access Token을 갱신합니다.")
    @Parameter(name = "Authorization", description = "jwt accessToken", required = true, example = "eyJh4444OiJIUzI1NiJ9.eyJ1c2VySWR4IjoxLCJyb2xlIjoidXNlciIsImlhdCI6MTcxNDc0Nj4444wiZXhwIjoxNzE0NzQ4NDg0fQ.gfbDplqgtNoYGI3tm4444ie2x-5VovulgjMQ8HAkYY4")
    @PostMapping("/refresh")
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
