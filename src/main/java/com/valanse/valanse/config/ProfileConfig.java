package com.valanse.valanse.config;

import com.valanse.valanse.repository.jpa.CategoryStatisticsRepository;
import com.valanse.valanse.repository.jpa.QuizCategoryRepository;
import com.valanse.valanse.repository.jpa.QuizRepository;
import com.valanse.valanse.repository.jpa.RecommendQuizRepository;
import com.valanse.valanse.repository.jpa.UserAnswerRepository;
import com.valanse.valanse.security.util.JwtUtil;
import com.valanse.valanse.service.ImageService.S3ImageService;
import com.valanse.valanse.service.QuizService.QuizService;
import com.valanse.valanse.service.QuizService.QuizServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class ProfileConfig {

    @Bean
    @Profile("local")
    public QuizService quizService(QuizRepository quizRepository,
                                   QuizCategoryRepository quizCategoryRepository,
                                   UserAnswerRepository userAnswerRepository,
                                   CategoryStatisticsRepository categoryStatisticsRepository,
                                   S3ImageService s3ImageService,
                                   JwtUtil jwtUtil,
                                   RecommendQuizRepository recommendQuizRepository) {

        return new QuizServiceImpl(quizRepository,
                quizCategoryRepository,
                userAnswerRepository,
                categoryStatisticsRepository,
                s3ImageService,
                jwtUtil,
                recommendQuizRepository);
    }
}