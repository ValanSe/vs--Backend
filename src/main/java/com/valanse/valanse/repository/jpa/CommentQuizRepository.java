package com.valanse.valanse.repository.jpa;

import com.valanse.valanse.entity.CommentQuiz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentQuizRepository extends JpaRepository<CommentQuiz, Integer> {
    List<CommentQuiz> findByQuizId(Integer quizId);
}
