package com.valanse.valanse.security.util;

import com.valanse.valanse.exception.InvalidStateTokenException;
import com.valanse.valanse.redis.entity.AccessToken;
import com.valanse.valanse.redis.repository.AccessTokenRepository;
import com.valanse.valanse.redis.repository.RefreshTokenRepository;
import com.valanse.valanse.redis.service.TokenService;
import com.valanse.valanse.security.dto.GeneratedTokenDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtUtil {

    private final TokenService tokenService;
    private final AccessTokenRepository accessTokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final StateTokenUtil stateTokenUtil;

    @Value("${jwt.secret}")
    private String stringSecretKey;

    private Key secretKey;

    @PostConstruct
    public void init() {
        // stringSecretKey를 Key 객체로 변환하여 secretKey에 할당
        secretKey = Keys.hmacShaKeyFor(stringSecretKey.getBytes(StandardCharsets.UTF_8));
    }

    public GeneratedTokenDto generateToken(Integer userIdx, String role) {
        // accessToken 발급을 위한 stateToken을 생성한다.
        // stateToken은 엑세스 토큰 발급을 위한 일회용 토큰이다.
        String stateToken = stateTokenUtil.getStateToken();

        // accessToken과 refreshToken을 생성한다.
        String accessToken = generateAccessToken(userIdx, role);
        String refreshToken = generateRefreshToken(userIdx, role);

        // 토큰을 Redis에 저장한다.
        tokenService.saveAccessTokenInfo(stateToken, accessToken);
        // userIdx를 키로 하여 refreshToken을 저장한다. accessToken은 인덱싱을 위한 메타데이터로 사용되기도 한다.
        tokenService.saveRefreshTokenInfo(userIdx, accessToken, refreshToken);
        return new GeneratedTokenDto(stateToken, accessToken, refreshToken);
    }

    public String generateRefreshToken(int userIdx, String role) {
        // 토큰의 유효 기간을 밀리초 단위로 설정.
        long refreshPeriod = 1000L * 60L * 60L * 24L * 14; // 2주

        // 새로운 클레임 객체를 생성하고, 이메일과 역할(권한)을 셋팅
        Claims claims = Jwts.claims();
        claims.put("userIdx", userIdx);
        claims.put("role", role);

        // 현재 시간과 날짜를 가져온다.
        Date now = new Date();

        return Jwts.builder()
                // Payload를 구성하는 속성들을 정의한다.
                .setClaims(claims)
                // 발행일자를 넣는다.
                .setIssuedAt(now)
                // 토큰의 만료일시를 설정한다.
                .setExpiration(new Date(now.getTime() + refreshPeriod))
                // 지정된 서명 알고리즘과 비밀 키를 사용하여 토큰을 서명한다.
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }


    public String generateAccessToken(int userIdx, String role) {
        long tokenPeriod = 1000L * 60L * 30L; // 30분
//        long tokenPeriod = 1000L * 2; // 2초 테스트
        Claims claims = Jwts.claims();
        claims.put("userIdx", userIdx);
        claims.put("role", role);

        Date now = new Date();
        return Jwts.builder()
                // Payload를 구성하는 속성들을 정의한다.
                .setClaims(claims)
                // 발행일자를 넣는다.
                .setIssuedAt(now)
                // 토큰의 만료일시를 설정한다.
                .setExpiration(new Date(now.getTime() + tokenPeriod))
                // 지정된 서명 알고리즘과 비밀 키를 사용하여 토큰을 서명한다.
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();

    }


    public boolean verifyToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey) // 비밀키를 설정하여 파싱한다.
                    .build()
                    .parseClaimsJws(token);  // 주어진 토큰을 파싱하여 Claims 객체를 얻는다.
            // 토큰의 만료 시간과 현재 시간비교
            return claims.getBody()
                    .getExpiration()
                    .after(new Date());  // 만료 시간이 현재 시간 이후인지 확인하여 유효성 검사 결과를 반환
        } catch (Exception e) {
            return false;
        }
    }

    @Transactional
    public String getAccessTokenByStateToken(String stateToken) {
        String accessToken = accessTokenRepository.findByStateToken(stateToken)
                .orElseThrow(() -> new InvalidStateTokenException("Invalid state token provided."))
                .getAccessToken();

        accessTokenRepository.deleteById(stateToken);

        return accessToken; // AccessToken 객체에서 액세스 토큰 문자열 반환
    }

    // 토큰에서 USERIDX(유저 식별자)만 추출한다.
    public int getUserIdx(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("userIdx", Integer.class);
    }

    // 토큰에서 ROLE(권한)만 추출한다.
    public String getUserRole(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }

    /**
     * 토큰의 남은 유효 시간을 분 단위로 반환한다.
     *
     * @param token 검사할 토큰
     * @return 남은 유효 시간(분)
     */
    public long getRemainingExpirationTimeInMinutes(String token) {
        Date expiration = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        long diff = expiration.getTime() - System.currentTimeMillis();
        return diff / (60 * 1000); // 밀리초를 분으로 변환
    }

    /**
     * 토큰에 설정된 모든 클레임을 Map으로 반환한다.
     *
     * @param token 조회할 토큰
     * @return 클레임 Map
     */
    public Map<String, Object> getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


}