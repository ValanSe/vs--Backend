package com.valanse.valanse.repository.jpa;

import com.valanse.valanse.entity.UserAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientResponseRepository extends JpaRepository<UserAnswer, Integer> {
}