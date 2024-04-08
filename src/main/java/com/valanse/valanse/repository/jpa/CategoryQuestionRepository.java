package com.valanse.valanse.repository.jpa;
import com.valanse.valanse.entity.QuizCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryQuestionRepository extends JpaRepository<QuizCategory, Integer> {
}
