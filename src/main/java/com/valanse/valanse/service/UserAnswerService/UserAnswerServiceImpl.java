package com.valanse.valanse.service.UserAnswerService;

import com.valanse.valanse.entity.QuizCategory;
import com.valanse.valanse.entity.UserAnswer;
import com.valanse.valanse.entity.UserCategoryPreference;
import com.valanse.valanse.entity.UserCategoryPreferenceId;
import com.valanse.valanse.repository.jpa.QuizCategoryRepository;
import com.valanse.valanse.repository.jpa.UserCategoryPreferenceRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserAnswerServiceImpl implements UserAnswerService {

    private final QuizCategoryRepository quizCategoryRepository;
    private final UserCategoryPreferenceRepository userCategoryPreferenceRepository;
    private final EntityManager entityManager;


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateUserCategoryPreference(UserAnswer userAnswer) {
        try {
            Integer userId = userAnswer.getUserId();
            log.info("Updating user category preference for user ID: {}", userId);

            List<QuizCategory> quizCategories = quizCategoryRepository.findByQuizId(userAnswer.getQuizId());
            List<String> categories = quizCategories.stream()
                    .map(QuizCategory::getCategory)
                    .collect(Collectors.toList());

            if (categories.isEmpty()) {
                throw new EntityNotFoundException();
            }

            List<UserCategoryPreference> ucps = getUserCategoryPreferences(userId, categories);
            for (UserCategoryPreference ucp : ucps) {
                log.info("ucp for user ID: {} with category: {}", userId, ucp.getCategory());
            }

            Integer preference = userAnswer.getPreference();
            log.info("Updating preferences for user ID: {} with preference: {}", userId, preference);

            updatePreferences(ucps, preference);
            log.info("User category preferences updated for user ID: {}", userId);

        } catch (EntityNotFoundException e) {
            log.error("No categories found for quiz ID: " + userAnswer.getQuizId());
        } catch (Exception e) {
            log.error("Error updating user category preference", e);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<UserCategoryPreference> getUserCategoryPreferences(Integer userId, List<String> categories) {
        List<UserCategoryPreference> preferences = new ArrayList<>();
        for (String category : categories) {
            UserCategoryPreferenceId id = new UserCategoryPreferenceId(userId, category);
            UserCategoryPreference preference = userCategoryPreferenceRepository.findById(id).orElseGet(() -> {
                UserCategoryPreference newPreference = UserCategoryPreference.builder()
                        .userId(userId)
                        .category(category)
                        .answerCount(0)
                        .totalPreference(0)
                        .commentCount(0)
                        .registrationCount(0)
                        .build();
                return userCategoryPreferenceRepository.save(newPreference);
            });
            preferences.add(preference);
        }
        return preferences;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updatePreferences(List<UserCategoryPreference> preferences, Integer preference) {
        for (UserCategoryPreference userCategoryPreference : preferences) {
            UserCategoryPreference updatedPreference = UserCategoryPreference.builder()
                    .userId(userCategoryPreference.getUserId())
                    .category(userCategoryPreference.getCategory())
                    .answerCount(userCategoryPreference.getAnswerCount() + 1)
                    .totalPreference(userCategoryPreference.getTotalPreference() + preference)
                    .commentCount(userCategoryPreference.getCommentCount())
                    .registrationCount(userCategoryPreference.getRegistrationCount())
                    .build();
            log.info("Updating preference: userId: {}, category: {}", updatedPreference.getUserId(), updatedPreference.getCategory());
            userCategoryPreferenceRepository.save(updatedPreference);
            log.info("success update UCP: userId: {}, category: {}", updatedPreference.getUserId(), updatedPreference.getCategory());
        }
    }
}
