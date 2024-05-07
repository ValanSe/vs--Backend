package com.valanse.valanse.service.QuizCategoryService;

import ch.qos.logback.classic.Logger;
import com.valanse.valanse.entity.Quiz;
import com.valanse.valanse.entity.QuizCategory;
import com.valanse.valanse.repository.jpa.QuizCategoryRepository;
import com.valanse.valanse.repository.jpa.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuizCategoryServicelmpl implements QuizCategoryService {

    private final QuizCategoryRepository quizCategoryRepository;
    private final QuizRepository quizRepository;
    private Logger log;

    @Override
    public void addQuizToCategory(QuizCategory category, Quiz quiz) throws IllegalAccessException {
        try {
            Integer categoryId = category.getCategoryId();
            Integer quizId = quiz.getQuizId();

            QuizCategory existingCategory = quizCategoryRepository.findById(categoryId)
                    .orElseThrow(() -> new NoSuchElementException("Category not found: " + categoryId));
            if (existingCategory.getQuizIdList().contains(quizId)) {
                throw new IllegalAccessException("Quiz with ID " + quizId + "already exists in category" + categoryId);
            }
            existingCategory.getQuizIdList().add(quiz.getQuizId());
            quizCategoryRepository.save(existingCategory);
        } catch (Exception e) {
            log.error("Error retrieving category {}: {}", category, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public List<QuizCategory> searchCategory(String keyword) {
        return quizCategoryRepository.findByCategoryContaining(keyword);
    }

    @Override
    public Optional<Integer> getQuizCountByCategory(QuizCategory category) {
        try {
            Integer categoryId = category.getCategoryId();

            QuizCategory existingCategory = quizCategoryRepository.findById(categoryId)
                    .orElseThrow(() -> new NoSuchElementException("Category not found: " + categoryId));

            return Optional.of(existingCategory.getQuizIdList().size());
        } catch (Exception e) {
            log.error("Error retrieving category {}: {}", category, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public Optional<Double> getAveragePreferenceByCategory(QuizCategory category) {
        try {
            Integer categoryId = category.getCategoryId();

            QuizCategory existingCategory = quizCategoryRepository.findById(categoryId)
                    .orElseThrow(() -> new NoSuchElementException("Category not found: " + categoryId));

            List<Integer> quizIdList = existingCategory.getQuizIdList();
            List<Quiz> quizzes = quizRepository.findAllById(quizIdList);

            if (quizzes.isEmpty())
                return Optional.empty();
            double perferenceSum = quizzes.stream()
                    .mapToInt(Quiz::getPreference)
                    .sum();
            Double averagePreference = (Double) perferenceSum / quizzes.size();
            return Optional.of(averagePreference);
        } catch (Exception e) {
            log.error("Error retrieving category {}: {}", category, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public Optional<Integer> getViewsCountByCategory(QuizCategory category) {
        try {
            Integer categoryId = category.getCategoryId();

            QuizCategory existingCategory = quizCategoryRepository.findById(categoryId)
                    .orElseThrow(() -> new NoSuchElementException("Category not found: " + categoryId));

            List<Integer> quizIdList = existingCategory.getQuizIdList();
            List<Quiz> quizzes = quizRepository.findAllById(quizIdList);

            if (quizzes.isEmpty())
                return Optional.empty();
            Integer viewsSum = quizzes.stream()
                    .mapToInt(Quiz::getView)
                    .sum();
            return Optional.of(viewsSum);
        } catch (Exception e) {
            log.error("Error retrieving category {}: {}", category, e.getMessage(), e);
            throw e;
        }
    }
}
