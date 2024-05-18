package com.valanse.valanse.service.QuizService;

import com.valanse.valanse.dto.QuizRegisterDto;
import com.valanse.valanse.dto.UserAnswerDto;
import com.valanse.valanse.dto.QuizDto;
import com.valanse.valanse.entity.Quiz;
import com.valanse.valanse.entity.Category;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface QuizService {

    QuizDto getQuiz(Integer quizId);

    void increasePreference(Integer quizId, int preference); // 퀴즈의 선호도 증가

    Optional<Integer> getQuizPreference(Integer quizId); // 퀴즈의 선호도 조회

    Optional<Integer> getViewsCount(Integer quizId); // 퀴즈의 조회수 조회

    List<Quiz> sortQuizByCreatedAt(); // 생성 시간에 따른 퀴즈 정렬

    List<Quiz> sortQuizByPreference(); // 선호도에 따른 퀴즈 정렬

    List<Quiz> searchQuiz(String keyword); // 퀴즈 검색

    void saveUserAnswer(UserAnswerDto userAnswer); // 클라이언트의 답변을 데이터베이스에 저장

    void registerQuiz(HttpServletRequest httpServletRequest, QuizRegisterDto quizRegisterDto, MultipartFile image_A, MultipartFile image_B) throws IOException;

}
