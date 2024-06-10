package com.valanse.valanse.service.UserAnswerService;

import com.valanse.valanse.entity.*;
import com.valanse.valanse.repository.jpa.QuizCategoryRepository;
import com.valanse.valanse.repository.jpa.UserAnswerRepository;
import com.valanse.valanse.repository.jpa.UserCategoryPreferenceRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserAnswerServiceImpl implements UserAnswerService {

    private final UserAnswerRepository userAnswerRepository;
    private final QuizCategoryRepository quizCategoryRepository;
    private final UserCategoryPreferenceRepository userCategoryPreferenceRepository;


    @Override
    public List<Quiz> getUserPreferences(Integer userId, int preference) {
        return userAnswerRepository.findByUserIdAndPreferenceGreaterThanEqual(userId, preference);
    }

    @Transactional
    public void updateUserCategoryPreference(UserAnswer userAnswer) {
        try {
            Integer userId = userAnswer.getUserId();

            List<QuizCategory> quizCategories = quizCategoryRepository.findByQuizId(userAnswer.getQuizId());
            List<String> categories = quizCategories.stream()
                    .map(QuizCategory::getCategory)
                    .collect(Collectors.toList());

            if (categories.isEmpty()) {
                throw new EntityNotFoundException();
            }

            userCategoryPreferenceRepository.incrementCommentCounts(userId, categories);

            initUserCategoryPreference(userId, categories);
            Integer preference = userAnswer.getPreference();

            userCategoryPreferenceRepository.updatePreferences(userId, categories, preference);

            log.info("User category preferences updated for user ID: {}", userId);
        } catch (EntityNotFoundException e) {
            log.error("No categories found for quiz ID: " + userAnswer.getQuizId());
        } catch (Exception e) {
            log.error("Error updating user category preference", e);
        }
    }

    public void initUserCategoryPreference(Integer userId, List<String> categoriesByQuizId) {
        try {
            for (String category : categoriesByQuizId) {
                UserCategoryPreferenceId userCategoryPreferenceId = new UserCategoryPreferenceId(userId, category);
                userCategoryPreferenceRepository.findById(userCategoryPreferenceId).orElseGet(() ->
                        userCategoryPreferenceRepository.save(
                                UserCategoryPreference.builder()
                                        .userId(userId)
                                        .category(category)
                                        .answerCount(0)
                                        .totalPreference(0)
                                        .commentCount(0)
                                        .registrationCount(0)
                                        .recommendScore(0)
                                        .build()));
            }
            log.info("User category preferences initialized for user ID: {}", userId);
        } catch (Exception e) {
            log.error("Error initializing user category preferences for user ID: {}", userId, e);
        }
    }
}
