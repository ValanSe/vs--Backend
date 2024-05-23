package com.valanse.valanse.redis.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@AllArgsConstructor
@RedisHash(value = "AccessToken", timeToLive = 20)
public class AccessToken {
    @Id
    private String stateToken; // 상태 토큰을 ID로 사용
    private String accessToken; // 엑세스 토큰 값
}
