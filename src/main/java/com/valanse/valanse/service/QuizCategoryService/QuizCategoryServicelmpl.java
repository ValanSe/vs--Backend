package com.valanse.valanse.service.QuizCategoryService;

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
    public int getQuizCountByCategory(String category) {
        try {
             List<QuizCategory> quizzesInCategory = quizCategoryRepository.findByCategory(category);

             if (quizzesInCategory.isEmpty()) {
                 throw new EntityNotFoundException("Quiz Category not found: " + category);
             }

             return quizzesInCategory.size();
        } catch (Exception e) {
            log.error("Error retrieving category {}: {}", category, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public double getAveragePreferenceByCategory(String category) {
        try {
            List<QuizCategory> quizzesInCategory = quizCategoryRepository.findByCategory(category);

            if (quizzesInCategory.isEmpty()) {
                throw new EntityNotFoundException("Quiz Category not found: " + category);
            }

            double totalPreference = 0.0;

            for (QuizCategory quizCategory : quizzesInCategory) {
                Quiz quiz = quizRepository.findById(quizCategory.getQuizId()).orElseThrow(EntityNotFoundException::new);

                totalPreference += quiz.getPreference();
            }

            return totalPreference / quizzesInCategory.size();

        } catch (Exception e) {
            log.error("Error retrieving category {}: {}", category, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public int getViewsCountByCategory(String category) {
        try {
            List<QuizCategory> quizzesInCategory = quizCategoryRepository.findByCategory(category);

            if (quizzesInCategory.isEmpty()) {
                throw new EntityNotFoundException("Quiz Category not found: " + category);
            }

            int totalView = 0;

            for (QuizCategory quizCategory : quizzesInCategory) {
                Quiz quiz = quizRepository.findById(quizCategory.getQuizId()).orElseThrow(EntityNotFoundException::new);

                totalView += quiz.getView();
            }

            return totalView;

        } catch (Exception e) {
            log.error("Error retrieving category {}: {}", category, e.getMessage(), e);
            throw e;
        }
    }
}
