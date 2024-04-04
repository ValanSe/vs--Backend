package com.valanse.valanse.repository.jpa;
import com.valanse.valanse.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Quiz, Integer> {

}
