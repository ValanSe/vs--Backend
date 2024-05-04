package com.valanse.valanse.service.UserAnswerService;

import com.valanse.valanse.entity.Quiz;

import java.util.List;
import java.util.Optional;

public interface UserAnswerService {

    List<Quiz> getUserPreferences(Integer userId, int preference); // 사용자가 선호도 준 퀴즈 조회
}