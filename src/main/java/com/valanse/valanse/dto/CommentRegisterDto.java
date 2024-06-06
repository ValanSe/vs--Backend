package com.valanse.valanse.dto;

import lombok.*;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentRegisterDto {
    private String content;

    private Integer quizId;
}
