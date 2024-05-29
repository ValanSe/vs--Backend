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
    @Query("UPDATE  Quiz q SET q.viewCount = q.viewCount + 1 WHERE q.quizId = :quizId")
    void increaseViewCount(@Param("quizId") Integer quizId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE quiz SET preference = preference + 1, like_count = like_count + 1 WHERE quiz_id = :quizId", nativeQuery = true)
    void increasePreference(@Param("quizId") Integer quizId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE quiz SET preference = preference - 1, unlike_count = unlike_count + 1 WHERE quiz_id = :quizId", nativeQuery = true)
    void decreasePreference(@Param("quizId") Integer quizId);

    List<Quiz> findAllByOrderByCreatedAtDesc();

    List<Quiz> findAllByOrderByPreferenceDesc();

    List<Quiz> findByContentContaining(String keyword);
}