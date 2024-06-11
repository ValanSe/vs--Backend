package com.valanse.valanse.repository.jpa;

import com.valanse.valanse.entity.Quiz;
import com.valanse.valanse.entity.UserAnswer;
import com.valanse.valanse.entity.UserAnswerId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserAnswerRepository extends JpaRepository<UserAnswer, UserAnswerId> {

    Optional<UserAnswer> findByUserIdAndQuizId(Integer userId, Integer quizId);

}