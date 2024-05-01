package com.valanse.valanse.redis.service;

import com.valanse.valanse.redis.entity.AccessToken;
import com.valanse.valanse.redis.repository.AccessTokenRepository;
import com.valanse.valanse.redis.repository.RefreshTokenRepository;
import com.valanse.valanse.redis.entity.RefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final AccessTokenRepository accessTokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;


    //Redis 작업

    @Transactional
    public void saveAccessTokenInfo(String stateToken, String accessToken) {
        accessTokenRepository.save(new AccessToken(stateToken, accessToken));
    }

    @Transactional
    public void removeAccessToken(String stateToken) {
        AccessToken accessToken = accessTokenRepository.findByStateToken(stateToken)
                .orElseThrow(IllegalArgumentException::new);
        accessTokenRepository.deleteById(stateToken);
    }

    @Transactional
    public void saveRefreshTokenInfo(Integer userIdx, String accessToken, String refreshToken) {
        refreshTokenRepository.save(new RefreshToken(userIdx, accessToken, refreshToken));
    }

    @Transactional
    public void removeRefreshToken(String accessToken) {
        RefreshToken refreshToken = refreshTokenRepository.findByAccessToken(accessToken)
                .orElseThrow(IllegalArgumentException::new);
        refreshTokenRepository.delete(refreshToken);
    }

}
