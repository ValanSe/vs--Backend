package com.valanse.valanse.service.QuizService;

import com.valanse.valanse.dto.UserAnswerDto;
import com.valanse.valanse.dto.QuizDto;
import com.valanse.valanse.entity.Quiz;
import com.valanse.valanse.entity.Category;

import java.util.List;
import java.util.Optional;

public interface QuizService {
    List<Quiz> getUserPreferences(Integer userId, int preference); // 사용자가 선호도 준 퀴즈 조회
    Optional<Integer> getQuizPreferenceSum(Integer quizId); // 퀴즈의 선호도 합 조회
    Optional<Integer> getRecentActivity(Quiz quiz); // 퀴즈의 최근 활동(댓글 수, 조회수 등) 조회
    List<Quiz> sortQuizByCreatedAt(); // 생성 시간에 따른 퀴즈 정렬
    List<Quiz> sortQuizByPreference(); // 선호도에 따른 퀴즈 정렬
    List<Quiz> searchQuiz(String keyword); // 퀴즈 검색
    void saveUserAnswer(UserAnswerDto userAnswer); // 클라이언트의 답변을 데이터베이스에 저장
}
