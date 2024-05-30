package com.valanse.valanse.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.IdClass;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(UserPreferenceId.class)
public class UserPreference {
    @Id
    private Integer userId;
    @Id
    private Integer quizId;

    private String status;
}
