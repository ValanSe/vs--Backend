package com.valanse.valanse.repository;


import com.valanse.valanse.entity.NaverUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NaverUserRepository extends JpaRepository<NaverUser, String> {
}
