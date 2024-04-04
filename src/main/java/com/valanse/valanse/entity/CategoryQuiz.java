package com.valanse.valanse.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryQuiz {
    private Category category;
    private Quiz quiz;
}