package com.valanse.valanse.repository.jpa;

import com.valanse.valanse.entity.Quiz;
import com.valanse.valanse.entity.QuizCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QuizCategoryRepository extends JpaRepository<QuizCategory, Integer> {

    List<QuizCategory> findByCategoryContaining(String keyword);
}
