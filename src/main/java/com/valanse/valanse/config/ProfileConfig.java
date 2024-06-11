package com.valanse.valanse.config;

import com.valanse.valanse.repository.jpa.*;
import com.valanse.valanse.security.util.JwtUtil;
import com.valanse.valanse.service.ImageService.S3ImageService;
import com.valanse.valanse.service.QuizService.QuizService;
import com.valanse.valanse.service.QuizService.QuizServiceImpl;
import org.springframework.context.ApplicationEventPublisher;
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
                                   RecommendQuizRepository recommendQuizRepository,
                                   UserCategoryPreferenceRepository userCategoryPreferenceRepository,
                                   ApplicationEventPublisher applicationEventPublisher

    ) {

        return new QuizServiceImpl(quizRepository,
                quizCategoryRepository,
                userAnswerRepository,
                categoryStatisticsRepository,
                s3ImageService,
                jwtUtil,
                recommendQuizRepository,
                userCategoryPreferenceRepository,
                applicationEventPublisher);
    }
}