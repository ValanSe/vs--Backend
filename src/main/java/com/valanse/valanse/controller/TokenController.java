package com.valanse.valanse.controller;

import com.valanse.valanse.dto.StatusResponseDto;
import com.valanse.valanse.dto.response.TokenResponseStatus;
import com.valanse.valanse.redis.repository.RefreshTokenRepository;
import com.valanse.valanse.redis.entity.RefreshToken;
import com.valanse.valanse.redis.service.TokenService;
import com.valanse.valanse.security.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/token")
@Tag(name = "Token Controller", description = "토큰 관련 API를 관리합니다")
public class TokenController {


    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenService tokenService;
    private final JwtUtil jwtUtil;

    @Operation(summary = "테스트 엔드포인트", description = "토큰 컨트롤러의 테스트 엔드포인트입니다.")
    @ApiResponse(responseCode = "200", description = "테스트 성공", content = @Content(schema = @Schema(implementation = StatusResponseDto.class)))
    @GetMapping("/test")
    public ResponseEntity<StatusResponseDto> test() {
        return ResponseEntity.ok(StatusResponseDto.success("05230421"));
    }

    @Operation(summary = "Access Token 획득", description = "상태 토큰을 기반으로 Access Token을 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "액세스 토큰 반환 성공", content = @Content(schema = @Schema(implementation = StatusResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 상태 토큰")
    })
    @PostMapping("/get")
    public ResponseEntity<StatusResponseDto> getAccessToken(
            @RequestHeader("stateToken")
            @Parameter(description = "OAuth 로그인 후 얻을 수 있는 상태 토큰", required = true, example = "bc5a447a-db14-4a48-4f0e-4471712c168f") final String stateToken) {
        return ResponseEntity.ok(StatusResponseDto.success(jwtUtil.getAccessTokenByStateToken(stateToken)));
    }

    @Operation(summary = "Access Token 유효 기간 확인", description = "Access Token의 남은 유효 기간을 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "유효 기간 반환 성공", content = @Content(schema = @Schema(implementation = StatusResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 액세스 토큰")
    })
    @PostMapping("/check/expiration")
    public ResponseEntity<StatusResponseDto> check1(
            @RequestHeader("Authorization")
            @Parameter(description = "JWT 액세스 토큰", required = true, example = "eyJ...HAkYY4") final String accessToken) {
        long remainingExpirationTimeInMinutes = jwtUtil.getRemainingExpirationTimeInMinutes(accessToken);
        return ResponseEntity.ok(StatusResponseDto.success(remainingExpirationTimeInMinutes));
    }

    @Operation(summary = "Access Token의 모든 클레임 조회", description = "Access Token의 모든 클레임을 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "모든 클레임 반환 성공", content = @Content(schema = @Schema(implementation = StatusResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 액세스 토큰")
    })
    @PostMapping("/check/allClaimsFromToken")
    public ResponseEntity<StatusResponseDto> check2(
            @RequestHeader("Authorization")
            @Parameter(description = "JWT 액세스 토큰", required = true, example = "eyJ...HAkYY4") final String accessToken) {
        Map<String, Object> allClaimsFromToken = jwtUtil.getAllClaimsFromToken(accessToken);
        return ResponseEntity.ok(StatusResponseDto.success(allClaimsFromToken));
    }

    @Operation(summary = "유저 로그아웃 및 Refresh Token 삭제", description = "Access Token과 매치되는 Refresh Token을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그아웃 및 Refresh Token 삭제 성공", content = @Content(schema = @Schema(implementation = StatusResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 액세스 토큰")
    })
    @PostMapping("/logout")
    public ResponseEntity<StatusResponseDto> logout(
            @RequestHeader("Authorization")
            @Parameter(description = "JWT 액세스 토큰", required = true, example = "eyJ...HAkYY4") final String accessToken) {
        tokenService.removeRefreshToken(accessToken);
        return ResponseEntity.ok(StatusResponseDto.success());
    }

    @Operation(summary = "Access Token 갱신", description = "Refresh Token을 사용하여 Access Token을 갱신합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Access Token 갱신 성공", content = @Content(schema = @Schema(implementation = TokenResponseStatus.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 Refresh Token 또는 갱신 실패"),
            @ApiResponse(responseCode = "404", description = "Refresh Token을 찾을 수 없음"),
            @ApiResponse(responseCode = "401", description = "Refresh Token 만료")
    })
    @PostMapping("/refresh")
    public ResponseEntity<TokenResponseStatus> refresh(
            @RequestHeader("Authorization")
            @Parameter(description = "JWT 액세스 토큰", required = true, example = "eyJ...HAkYY4") final String accessToken) {
        try {
            RefreshToken refreshToken = refreshTokenRepository.findByAccessToken(accessToken)
                    .orElseThrow(() -> new EntityNotFoundException("Refresh Token not found for provided Access Token"));

            if (!jwtUtil.verifyToken(refreshToken.getRefreshToken())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(TokenResponseStatus.addStatus(401, "Refresh Token has expired"));
            }

            String newAccessToken = jwtUtil.generateAccessToken(refreshToken.getUserIdx(), jwtUtil.getUserRole(refreshToken.getRefreshToken()));
            refreshToken.updateAccessToken(newAccessToken);
            refreshTokenRepository.save(refreshToken);
            return ResponseEntity.ok(TokenResponseStatus.success(newAccessToken));

        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(TokenResponseStatus.addStatus(404, "Refresh Token not found"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(TokenResponseStatus.addStatus(400, "Invalid Refresh Token or failed to refresh"));
        }
    }
}
