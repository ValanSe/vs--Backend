package com.valanse.valanse.service.UserAnswerService;

import com.valanse.valanse.entity.Quiz;
import com.valanse.valanse.entity.UserAnswer;
import com.valanse.valanse.entity.UserCategoryPreference;
import com.valanse.valanse.entity.UserCategoryPreferenceId;
import com.valanse.valanse.repository.jpa.QuizCategoryRepository;
import com.valanse.valanse.repository.jpa.UserAnswerRepository;
import com.valanse.valanse.repository.jpa.UserCategoryPreferenceRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
            List<String> categoriesByQuizId = quizCategoryRepository.findByQuizId(userAnswer.getQuizId());

            if (categoriesByQuizId == null || categoriesByQuizId.isEmpty()) {
                throw new EntityNotFoundException();
            }

            initUserCategoryPreference(userId, categoriesByQuizId);
            Integer preference = userAnswer.getPreference();

            userCategoryPreferenceRepository.updatePreferences(userId, categoriesByQuizId, preference);

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
