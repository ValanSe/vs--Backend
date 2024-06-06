package com.valanse.valanse.dto;

import lombok.*;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentQuizDto {
    private Integer quizId;

    private Integer commentId;
}
