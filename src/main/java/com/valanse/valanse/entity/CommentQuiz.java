package com.valanse.valanse.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(CommentQuiz.class)
public class CommentQuiz {
    @Id
    private Integer quizId;

    @Id
    private Integer commentId;
}
