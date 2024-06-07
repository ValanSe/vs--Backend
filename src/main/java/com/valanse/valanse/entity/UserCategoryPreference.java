package com.valanse.valanse.entity;

import com.valanse.valanse.kafka.listener.UserCategoryPreferenceListener;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(UserCategoryPreferenceId.class)
@EntityListeners(UserCategoryPreferenceListener.class)
public class UserCategoryPreference {

    @Id
    private Integer userId;
    @Id
    private String category;

    private Integer answerCount;
    private Integer totalPreference;
    private Integer commentCount;
    private Integer registrationCount;
    private Integer recommendScore;
}
