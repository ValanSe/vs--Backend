package com.valanse.valanse.dto;

import lombok.*;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizCategoryDto {
    private String category;

    private Integer quizId;
}
