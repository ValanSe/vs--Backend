package com.valanse.valanse.event;

import com.valanse.valanse.service.UserAnswerService.UserAnswerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserAnswerEventListener {

    private final UserAnswerService userAnswerService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleUserAnswerEvent(UserAnswerEvent event) {
        try {
            userAnswerService.updateUserCategoryPreference(event.getUserAnswer());
        } catch (Exception e) {
            log.error("Error handling user answer event", e);
        }
    }
}