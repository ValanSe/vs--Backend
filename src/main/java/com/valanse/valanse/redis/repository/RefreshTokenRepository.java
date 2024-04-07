package com.valanse.valanse.redis.repository;

import com.valanse.valanse.security.dto.RefreshToken;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@EnableRedisRepositories
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Integer> {

    Optional<RefreshToken> findByAccessToken(String accessToken);

    Optional<RefreshToken> findByUserIdx(Integer userIdx);

}
