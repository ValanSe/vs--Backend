package com.valanse.valanse.repository.jpa;

import com.valanse.valanse.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuizRepository extends JpaRepository<Quiz, Integer> {

    @Modifying
    @Query("UPDATE  Quiz q SET q.view = q.view + 1 WHERE q.quizId = :quizId")
    void increaseView(@Param("quizId") Integer quizId);

    @Modifying
    @Query("UPDATE Quiz q SET q.preference = q.preference + :preference WHERE q.quizId = :quizId")
    void increasePreference(@Param("quizId") Integer quizId, @Param("preference") int preference);

    List<Quiz> findAllByOrderByCreatedAtDesc();

    List<Quiz> findAllByOrderByPreferenceDesc();

    List<Quiz> findByContentContaining(String keyword);
}