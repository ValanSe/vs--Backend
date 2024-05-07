package com.valanse.valanse.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(QuizCategoryId.class)
public class QuizCategory {
    @Id
    private String category;
    @Id
    private Integer quizId;

    private Integer categoryId;
    private List<Integer> quizIdList;
}
