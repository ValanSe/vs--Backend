package com.valanse.valanse.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(UserPreferenceId.class)
public class UserPreference {
    @Id
    private Integer quizId;

    @Id
    private Integer userId;

    private String status;
}
