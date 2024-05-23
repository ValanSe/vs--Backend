package com.valanse.valanse.repository.jpa;

import com.valanse.valanse.entity.QuizCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizCategoryRepository extends JpaRepository<QuizCategory, Integer> {

    void deleteByQuizId(Integer quizId);

    List<QuizCategory> findByCategory(String category);

    List<QuizCategory> findByCategoryContaining(String keyword);
}