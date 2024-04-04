package com.valanse.valanse.repository.jpa;
import com.valanse.valanse.entity.CategoryQuiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryQuestionRepository extends JpaRepository<CategoryQuiz, Integer> {
}
