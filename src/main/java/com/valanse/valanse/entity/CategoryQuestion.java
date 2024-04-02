package com.valanse.valanse.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer categoryQuestionId;

    @ManyToOne
    private Category category;

    @ManyToOne
    private Question question;
}