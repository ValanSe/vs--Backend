package com.valanse.valanse.redis.repository;

import com.valanse.valanse.redis.entity.AccessToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccessTokenRepository extends CrudRepository<AccessToken, String> {

        Optional<AccessToken> findByStateToken(String stateToken);

        void deleteById(String id);
}
