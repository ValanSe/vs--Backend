package com.valanse.valanse.service.UserAnswerService;

import com.valanse.valanse.config.SpringContext;
import com.valanse.valanse.entity.UserAnswer;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostUpdate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserAnswerListener {
    private UserAnswerService userAnswerService;


    @PostPersist
    @PostUpdate
    private void handleUserAnswerChange(UserAnswer userAnswer) {
        try {
            // 의존성 주입
            if (userAnswerService == null) {
                userAnswerService = SpringContext.getBean(UserAnswerService.class);
            }

            userAnswerService.updateUserCategoryPreference(userAnswer);
        } catch (Exception e) {
            log.error("Error handling user answer change", e);
        }
    }


}
