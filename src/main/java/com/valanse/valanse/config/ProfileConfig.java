package com.valanse.valanse.config;

import com.valanse.valanse.repository.jpa.QuizRepository;
import com.valanse.valanse.service.BalanceProblemService.QuizService;
import com.valanse.valanse.service.BalanceProblemService.QuizServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class ProfileConfig {

    @Bean
    @Profile("local")
    public QuizService quizService(QuizRepository quizRepository) {
        return new QuizServiceImpl(quizRepository);
    }
}