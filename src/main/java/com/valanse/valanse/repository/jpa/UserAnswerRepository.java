package com.valanse.valanse.repository.jpa;

import com.valanse.valanse.entity.Quiz;
import com.valanse.valanse.entity.UserAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserAnswerRepository extends JpaRepository<UserAnswer, Integer> {

    List<Quiz> findByUserIdAndPreferenceGreaterThanEqual(Integer userId, int preference);

    UserAnswer findByUserIdAndQuizId(Integer userId, Integer quizId);
}