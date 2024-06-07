package com.valanse.valanse.service.QuizService;

import com.valanse.valanse.dto.*;
import com.valanse.valanse.entity.Quiz;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface QuizService {

    Quiz getRandomQuiz();

    QuizDto getQuiz(int quizId);

    List<QuizDto> getAllQuiz();

    void registerQuiz(HttpServletRequest httpServletRequest, QuizRegisterDto quizRegisterDto, MultipartFile image_A, MultipartFile image_B) throws IOException;

    void updateQuiz(HttpServletRequest httpServletRequest, Integer quizId, QuizRegisterDto quizRegisterDto, MultipartFile image_A, MultipartFile image_B) throws IOException;

    void deleteQuiz(HttpServletRequest httpServletRequest, Integer quizId);

    QuizStatsDto getQuizStats(Integer quizId); // 퀴즈의 조회수, 선호도 조회

    QuizLikeStatsDto getQuizLikeStats(Integer quizId); // 퀴즈의 좋아요, 싫어요 수 조회

    List<Quiz> getMyQuizzes(HttpServletRequest httpServletRequest);

    List<Quiz> sortQuizByCreatedAt(); // 생성 시간에 따른 퀴즈 정렬

    List<Quiz> sortQuizByPreference(); // 선호도에 따른 퀴즈 정렬

    List<Quiz> searchQuiz(String keyword); // 퀴즈 검색

    void saveUserAnswer(UserAnswerDto userAnswer, String category) throws InvalidOptionException; // 클라이언트의 답변을 데이터베이스에 저장
}
