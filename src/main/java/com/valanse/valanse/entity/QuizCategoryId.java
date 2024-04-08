package com.valanse.valanse.entity;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class QuizCategoryId implements Serializable {
    private Integer category;
    private Integer quiz;
}
