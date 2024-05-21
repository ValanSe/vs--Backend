package com.valanse.valanse.config;

import com.valanse.valanse.repository.jpa.QuizCategoryRepository;
import com.valanse.valanse.repository.jpa.QuizRepository;
import com.valanse.valanse.repository.jpa.UserAnswerRepository;
import com.valanse.valanse.security.util.JwtUtil;
import com.valanse.valanse.service.BalanceProblemService.QuizService;
import com.valanse.valanse.service.BalanceProblemService.QuizServiceImpl;
import com.valanse.valanse.util.FileUploadUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class ProfileConfig {

    // TODO 더 복잡해지면 팩토리 패턴 사용 예정
    @Bean
    @Profile("local")
    public QuizService quizService(QuizRepository quizRepository,
                                   QuizCategoryRepository quizCategoryRepository,
                                   UserAnswerRepository userAnswerRepository,
                                   FileUploadUtil fileUploadUtil,
                                   JwtUtil jwtUtil) {

        return new QuizServiceImpl(quizRepository,
                quizCategoryRepository,
                userAnswerRepository,
                fileUploadUtil,
                jwtUtil);
    }
}