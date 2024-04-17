package com.valanse.valanse.repository;

import com.valanse.valanse.entity.KakaoUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KakaoUserRepository extends JpaRepository<KakaoUser, String> {
}
