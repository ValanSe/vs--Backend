package com.valanse.valanse.repository.jpa;

import com.valanse.valanse.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface QuizRepository extends JpaRepository<Quiz, Integer> {

    @Modifying
    @Transactional
    @Query("UPDATE  Quiz q SET q.view = q.view + 1 WHERE q.quizId = :quizId")
    void increaseView(@Param("quizId") Integer quizId);

    @Modifying
    @Transactional
    @Query("UPDATE Quiz q SET q.preference = q.preference + 1 WHERE q.quizId = :quizId")
    void increasePreference(@Param("quizId") Integer quizId);

    @Modifying
    @Transactional
    @Query("UPDATE Quiz q SET q.preference = q.preference - 1 WHERE q.quizId = :quizId")
    void decreasePreference(@Param("quizId") Integer quizId);

    List<Quiz> findAllByOrderByCreatedAtDesc();

    List<Quiz> findAllByOrderByPreferenceDesc();

    List<Quiz> findByContentContaining(String keyword);
}