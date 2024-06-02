package com.valanse.valanse.repository.jpa;

import com.valanse.valanse.entity.UserPreference;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPreferenceRepository extends JpaRepository<UserPreference, Integer> {
    UserPreference findByQuizIdAndUserId(Integer quizId, Integer userId);
}
