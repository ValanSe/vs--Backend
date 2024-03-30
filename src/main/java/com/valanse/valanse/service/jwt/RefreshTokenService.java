package com.valanse.valanse.service.jwt;

import com.valanse.valanse.repository.redis.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    //Redis 작업
    @Transactional
    public void saveTokenInfo(Integer userIdx, String accessToken, String refreshToken) {
        refreshTokenRepository.save(new RefreshToken(userIdx, accessToken, refreshToken));
    }

    @Transactional
    public void removeRefreshToken(String accessToken) {
        RefreshToken refreshToken = refreshTokenRepository.findByAccessToken(accessToken)
                .orElseThrow(IllegalArgumentException::new);
        refreshTokenRepository.delete(refreshToken);
    }
}
