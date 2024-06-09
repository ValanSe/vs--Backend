package com.valanse.valanse.service.QuizCategoryService;

import com.valanse.valanse.dto.QuizCategoryDto;
import com.valanse.valanse.repository.jpa.QuizCategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class QuizCategoryServicelmpl implements QuizCategoryService {

    private final QuizCategoryRepository quizCategoryRepository;

    @Override
    public List<QuizCategoryDto> getAllQuizByCategory(String category) {
        return quizCategoryRepository.findByCategory(category).stream()
                .map(quizCategory -> QuizCategoryDto.builder()
                        .category(quizCategory.getCategory())
                        .quizId(quizCategory.getQuizId())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<QuizCategoryDto> searchCategory(String keyword) {
        return quizCategoryRepository.findByCategoryContaining(keyword).stream()
                .map(quizCategory -> QuizCategoryDto.builder()
                        .category(quizCategory.getCategory())
                        .quizId(quizCategory.getQuizId())
                        .build())
                .collect(Collectors.toList());
    }
}