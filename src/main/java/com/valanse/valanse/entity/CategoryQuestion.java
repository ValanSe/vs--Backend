package com.valanse.valanse.entity;
import com.valanse.valanse.entity.Category;
import com.valanse.valanse.entity.Question;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryQuestion {
    //@Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    //private Integer categoryQuestionId;
    private Category category;
    private Question question;
}