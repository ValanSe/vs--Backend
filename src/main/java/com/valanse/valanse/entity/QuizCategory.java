package com.valanse.valanse.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(QuizCategoryId.class)
public class QuizCategory {
    @Id
    private Integer category;
    @Id
    private Integer quiz;
}
