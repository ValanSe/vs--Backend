package com.valanse.valanse.service.QuizCategoryService;

import com.valanse.valanse.dto.QuizCategoryStatsDto;
import com.valanse.valanse.entity.Quiz;
import com.valanse.valanse.entity.QuizCategory;
import com.valanse.valanse.repository.jpa.QuizCategoryRepository;
import com.valanse.valanse.repository.jpa.QuizRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class QuizCategoryServicelmpl implements QuizCategoryService {

    private final QuizCategoryRepository quizCategoryRepository;
    private final QuizRepository quizRepository;

    @Override
    public List<QuizCategory> searchCategory(String keyword) {
        return quizCategoryRepository.findByCategoryContaining(keyword);
    }

    @Override
    public QuizCategoryStatsDto getStatsByCategory(String category) {
        try {
            List<QuizCategory> quizzesInCategory = quizCategoryRepository.findByCategory(category);

            if (quizzesInCategory.isEmpty()) {
                throw new EntityNotFoundException("Quiz Category not found: " + category);
            }

            int totalView = 0;
            double totalPreference = 0.0;

            for (QuizCategory quizCategory : quizzesInCategory) {
                Quiz quiz = quizRepository.findById(quizCategory.getQuizId()).orElseThrow(EntityNotFoundException::new);

                totalView += quiz.getView();
                totalPreference += quiz.getPreference();
            }

            return QuizCategoryStatsDto.builder()
                    .quizCount(quizzesInCategory.size())
                    .viewsCount(totalView)
                    .averagePreference(totalPreference / quizzesInCategory.size())
                    .build();

        } catch (EntityNotFoundException e) {
            log.error("{} not found", category, e);
            throw e;
        }
    }
}