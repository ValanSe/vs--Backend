package com.valanse.valanse.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class UserPreferenceId implements Serializable {
    private Integer quizId;

    private Integer userId;
}
