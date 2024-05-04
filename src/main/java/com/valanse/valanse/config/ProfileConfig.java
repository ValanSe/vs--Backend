package com.valanse.valanse.config;

import com.valanse.valanse.repository.jpa.QuizRepository;
import com.valanse.valanse.security.util.JwtUtil;
import com.valanse.valanse.service.BalanceProblemService.QuizService;
import com.valanse.valanse.service.BalanceProblemService.QuizServiceImpl;
import com.valanse.valanse.util.FileUploadUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class ProfileConfig {

    @Bean
    @Profile("local")
    public QuizService quizService(QuizRepository quizRepository, FileUploadUtil fileUploadUtil, JwtUtil jwtUtil) {
        return new QuizServiceImpl(quizRepository, fileUploadUtil, jwtUtil);
    }
}