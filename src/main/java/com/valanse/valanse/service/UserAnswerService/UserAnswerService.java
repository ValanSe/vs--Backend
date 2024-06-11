package com.valanse.valanse.service.UserAnswerService;

import com.valanse.valanse.entity.Quiz;
import com.valanse.valanse.entity.UserAnswer;
import com.valanse.valanse.entity.UserCategoryPreference;

import java.util.List;
import java.util.Optional;

public interface UserAnswerService {

    void updateUserCategoryPreference(UserAnswer userAnswer);
    List<UserCategoryPreference> getUserCategoryPreferences(Integer userId, List<String> categoriesByQuizId);

}