package com.valanse.valanse.repository.jpa;

import com.valanse.valanse.entity.GoogleUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoogleUserRepository extends JpaRepository<GoogleUser, String> {
}
