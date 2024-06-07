package com.valanse.valanse.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class categoryStatistics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String category;

    private Integer totalAnswers;
    private Integer totalScore;
    private Float avgPreference;


}
