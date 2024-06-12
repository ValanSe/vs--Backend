package com.valanse.valanse.service.QuizService;

import com.valanse.valanse.dto.*;
import com.valanse.valanse.entity.Quiz;
import com.valanse.valanse.exception.InvalidOptionException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface QuizService {

    Quiz getRandomQuiz();

    QuizDto getQuiz(int quizId);

    List<QuizDto> getAllQuiz();
    List<QuizDto> getRecommendQuizzes(HttpServletRequest httpServletRequest);

    void registerQuiz(HttpServletRequest httpServletRequest, QuizRegisterDto quizRegisterDto, MultipartFile image_A, MultipartFile image_B);

    void updateQuiz(HttpServletRequest httpServletRequest, Integer quizId, QuizRegisterDto quizRegisterDto, MultipartFile image_A, MultipartFile image_B);

    void deleteQuiz(HttpServletRequest httpServletRequest, Integer quizId);

    QuizStatsDto getQuizStats(Integer quizId); // 퀴즈의 조회수, 선호도 조회

    List<QuizDto> getMyQuizzes(HttpServletRequest httpServletRequest);

    List<QuizDto> sortQuizByCreatedAt(); // 생성 시간에 따른 퀴즈 정렬

    List<QuizDto> sortQuizByPreference(); // 선호도에 따른 퀴즈 정렬

    List<QuizDto> searchQuiz(String keyword); // 퀴즈 검색

    void saveUserAnswer(HttpServletRequest httpServletRequest, UserAnswerDto userAnswer) throws InvalidOptionException; // 클라이언트의 답변을 데이터베이스에 저장

    Boolean checkUserAnswer(HttpServletRequest httpServletRequest, Integer quizId);
}
