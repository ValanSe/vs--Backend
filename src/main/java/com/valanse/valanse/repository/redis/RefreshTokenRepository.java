package com.valanse.valanse.repository.redis;

import com.valanse.valanse.service.jwt.RefreshToken;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Integer> {

    Optional<RefreshToken> findByAccessToken(String accessToken);

    Optional<RefreshToken> findByUserIdx(Integer userIdx);

}
