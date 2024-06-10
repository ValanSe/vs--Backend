package com.valanse.valanse.repository.jpa;

import com.valanse.valanse.entity.QuizCategory;
import com.valanse.valanse.entity.QuizCategoryId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizCategoryRepository extends JpaRepository<QuizCategory, QuizCategoryId> {

    void deleteByQuizId(Integer quizId);

    List<QuizCategory> findByCategory(String category);

    List<QuizCategory> findByQuizId(Integer quizId);

    List<QuizCategory> findByCategoryContaining(String keyword);
}