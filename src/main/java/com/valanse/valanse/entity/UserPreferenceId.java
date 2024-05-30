package com.valanse.valanse.entity;

import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserPreferenceId implements Serializable {
    private Integer userId;

    private Integer quizId;
}
