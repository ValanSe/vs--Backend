package com.valanse.valanse.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(RecommendQuizId.class)
public class RecommendQuiz {

    @Id
    private Integer userId;

    @Id
    private Integer quizId;
}

