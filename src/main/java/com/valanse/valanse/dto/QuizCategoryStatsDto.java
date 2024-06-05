package com.valanse.valanse.dto;

import lombok.*;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizCategoryStatsDto {
    private int quizCount;

    private int viewCount;

    private double averagePreference;
}
