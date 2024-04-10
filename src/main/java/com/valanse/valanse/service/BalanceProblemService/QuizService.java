package com.valanse.valanse.service.BalanceProblemService;

import com.valanse.valanse.dto.QuizDto;
import com.valanse.valanse.entity.Quiz;
import com.valanse.valanse.entity.UserAnswer;

public interface QuizService {

    Quiz getRandomQuiz();
    QuizDto getQuiz(int quizId);
    void saveUserAnswer(UserAnswer userAnswer); // 클라이언트의 답변을 데이터베이스에 저장하는 메서드
}