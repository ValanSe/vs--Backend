package com.valanse.valanse.event;

import com.valanse.valanse.entity.UserAnswer;

public class UserAnswerEvent {
    private final UserAnswer userAnswer;

    public UserAnswerEvent(UserAnswer userAnswer) {
        this.userAnswer = userAnswer;
    }

    public UserAnswer getUserAnswer() {
        return userAnswer;
    }
}