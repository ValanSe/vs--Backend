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

    @Query("SELECT q FROM Quiz q WHERE q.quizId IN :ids")
    List<Quiz> findAllByIdIn(@Param("ids") List<Integer> ids);

    @Query("SELECT q FROM Quiz q " +
                    "WHERE q.quizId IN :ids " +
                    "AND q.quizId NOT IN (SELECT ua.quizId FROM UserAnswer ua WHERE ua.userId = :userId)")
    List<Quiz> findAllByIdInAndNotAnswered(@Param("ids") List<Integer> ids, @Param("userId") Integer userId);


    @Modifying
    @Transactional
    @Query("UPDATE Quiz q" +
            " SET q.preference = q.preference + :preference WHERE q.quizId = :quizId")
    void updateQuizPreference(@Param("quizId") Integer quizId, @Param("preference") Integer preference);

    List<Quiz> findByAuthorUserId(Integer authorUserId);

    List<Quiz> findAllByOrderByCreatedAtDesc();

    List<Quiz> findAllByOrderByPreferenceDesc();

    List<Quiz> findByContentContaining(String keyword);
}