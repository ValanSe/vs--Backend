package com.valanse.valanse.service.QuizService;

import com.valanse.valanse.dto.UserAnswerDto;
import com.valanse.valanse.dto.QuizDto;
import com.valanse.valanse.entity.Quiz;
import com.valanse.valanse.entity.Category;

import java.util.List;
import java.util.Optional;

public interface QuizService {
    void addQuiz(QuizDto quiz); // 퀴즈 등록
    void updateQuiz(QuizDto quiz); // 퀴즈 업데이트
    void deleteQuiz(Integer quizId); // 퀴즈 삭제
    Optional<Quiz> findQuizById(Integer quizId); // 퀴즈 조회
    List<Quiz> findAllQuiz(); // 모든 퀴즈 조회
    List<Quiz> findQuizByCategory(Category category); // 특정 카테고리의 퀴즈 조회
    List<QuizDto> provideQuizToClient(); // 클라이언트에게 퀴즈 제공
    void saveClientResponse(UserAnswerDto userAnswer); // 클라이언트의 답변을 데이터베이스에 저장
}
