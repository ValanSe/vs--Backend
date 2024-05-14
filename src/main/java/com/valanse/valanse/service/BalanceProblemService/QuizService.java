package com.valanse.valanse.service.BalanceProblemService;

import com.valanse.valanse.dto.QuizDto;
import com.valanse.valanse.dto.QuizRegisterDto;
import com.valanse.valanse.entity.Quiz;
import com.valanse.valanse.entity.UserAnswer;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface QuizService {

    Quiz getRandomQuiz();

    QuizDto getQuiz(int quizId);

    void saveUserAnswer(UserAnswer userAnswer); // 클라이언트의 답변을 데이터베이스에 저장하는 메서드

    void registerQuiz(HttpServletRequest httpServletRequest, QuizRegisterDto quizRegisterDto, MultipartFile image_A, MultipartFile image_B) throws IOException;

    void deleteQuiz(HttpServletRequest httpServletRequest, int quizId) throws IOException;
}