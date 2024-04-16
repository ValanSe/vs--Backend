package com.valanse.valanse.service.QuizService;

import com.valanse.valanse.dto.QuizDto;
import com.valanse.valanse.dto.UserAnswerDto;
import com.valanse.valanse.entity.Category;
import com.valanse.valanse.entity.Quiz;
import com.valanse.valanse.repository.jpa.QuizRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuizServiceImpl implements QuizService {
    @Override
    public List<Quiz> findUserPreferences(Integer userId, int preference) {
        return null;
    }

    @Override
    public Optional<Integer> findQuizPreferenceSum(Integer quizId) {
        return Optional.empty();
    }

    @Override
    public Optional<Integer> findRecentActivity(Quiz quiz) {
        return Optional.empty();
    }


    @Override
    public List<QuizDto> provideQuizToClient() {
        return null;
    }

    @Override
    public void saveUserAnswer(UserAnswerDto userAnswer) {

    }

    @Override
    public List<Quiz> searchQuiz(String keyword) {
        return null;
    }

    @Override
    public List<Quiz> sortQuizByCreatedAt() {
        return null;
    }

    @Override
    public List<Quiz> sortQuizByPreference() {
        return null;
    }
}