package com.valanse.valanse.repository.jpa;

import com.valanse.valanse.entity.RecommendQuiz;
import com.valanse.valanse.entity.RecommendQuizId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecommendQuizRepository extends JpaRepository<RecommendQuiz, RecommendQuizId> {
    List<RecommendQuiz> findByUserId(int userId);
}
