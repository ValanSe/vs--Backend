package com.valanse.valanse.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Formula;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryStatistics {

    @Id
    private String category;

    private Integer totalAnswers; // 해당 카테고리에서 전체 사용자가 푼 문제 수

    private Integer totalScore; // 해당 카테고리에서 전체 사용자의 총 선호도 점수

    @Formula("total_score / total_answers")
    private Float avgPreference; // 해당 카테고리에서 전체 사용자의 평균 선호도
}
