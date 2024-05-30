package com.valanse.valanse.config;

import com.valanse.valanse.repository.jpa.QuizCategoryRepository;
import com.valanse.valanse.repository.jpa.QuizRepository;
import com.valanse.valanse.repository.jpa.UserAnswerRepository;
import com.valanse.valanse.repository.jpa.UserPreferenceRepository;
import com.valanse.valanse.security.util.JwtUtil;
import com.valanse.valanse.service.ImageService.S3ImageService;
import com.valanse.valanse.service.QuizService.QuizService;
import com.valanse.valanse.service.QuizService.QuizServiceImpl;
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
                                   UserPreferenceRepository userPreferenceRepository,
                                   S3ImageService s3ImageService,
                                   JwtUtil jwtUtil) {

        return new QuizServiceImpl(quizRepository,
                quizCategoryRepository,
                userAnswerRepository,
                userPreferenceRepository,
                s3ImageService,
                jwtUtil);
    }
}