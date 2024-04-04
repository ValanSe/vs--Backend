package com.valanse.valanse.repository.redis.repository;

import com.valanse.valanse.security.dto.RefreshToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Integer> {

    Optional<RefreshToken> findByAccessToken(String accessToken);

    Optional<RefreshToken> findByUserIdx(Integer userIdx);

}
