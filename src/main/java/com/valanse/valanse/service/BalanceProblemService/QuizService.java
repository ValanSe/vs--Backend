package com.valanse.valanse.service.BalanceProblemService;

import com.valanse.valanse.entity.Quiz;
import com.valanse.valanse.entity.UserAnswer;

public interface QuizService {

    Quiz getRandomQuiz();
    void saveUserAnswer(UserAnswer userAnswer); // 클라이언트의 답변을 데이터베이스에 저장하는 메서드
}