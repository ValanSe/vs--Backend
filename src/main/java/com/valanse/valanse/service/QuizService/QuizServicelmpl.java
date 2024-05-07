package com.valanse.valanse.service.QuizService;

import ch.qos.logback.classic.Logger;
import com.valanse.valanse.dto.QuizDto;
import com.valanse.valanse.dto.UserAnswerDto;
import com.valanse.valanse.entity.Quiz;
import com.valanse.valanse.entity.UserAnswer;
import com.valanse.valanse.repository.jpa.QuizRepository;
import com.valanse.valanse.repository.jpa.UserAnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuizServicelmpl implements QuizService {

    private final QuizRepository quizRepository;
    private final UserAnswerRepository userAnswerRepository;

    private Logger log;

    @Override
    public QuizDto getQuiz(Integer quizId) {
        try {
            Quiz quiz = quizRepository.findById(quizId)
                    .orElseThrow(() -> new QuizNotFoundException("Quiz not found with id: " + quizId));
            quizRepository.increaseView(quizId); // 조회수 증가
            return QuizDto.builder()
                    .quizId(quiz.getQuizId())
                    .authorUserId(quiz.getAuthorUserId())
                    .content(quiz.getContent())
                    .optionA(quiz.getOptionA())
                    .optionB(quiz.getOptionB())
                    .descriptionA(quiz.getDescriptionA())
                    .descriptionB(quiz.getDescriptionB())
                    .view(quiz.getView())
                    .preference(quiz.getPreference())
                    .createdAt(quiz.getCreatedAt())
                    .build();
        } catch (Exception e) {
            log.error("Error retrieving quiz with id {}: {}", quizId, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void increasePreference(Integer quizId, int preference) {
        try {
            Quiz quiz = quizRepository.findById(quizId)
                    .orElseThrow(() -> new QuizNotFoundException("Quiz not found with id: " + quizId));
            quizRepository.increasePreference(quizId, preference); // 선호도 수 증가
        } catch (Exception e) {
            log.error("Error retrieving quiz with id {}: {}", quizId, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public Optional<Integer> getQuizPreference(Integer quizId) {
        try {
            return Optional.ofNullable(quizRepository.findById(quizId)
                    .map(Quiz::getPreference)
                    .orElseThrow(() -> new QuizNotFoundException("Quiz not found with id: " + quizId)));
        } catch (Exception e) {
            log.error("Error retrieving quiz with id {}: {}", quizId, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public Optional<Integer> getViewsCount(Integer quizId) {
        try {
            return Optional.ofNullable(quizRepository.findById(quizId)
                    .map(Quiz::getView)
                    .orElseThrow(() -> new QuizNotFoundException("Quiz not found with id: " + quizId)));
        } catch (Exception e) {
            log.error("Error retrieving quiz with id {}: {}", quizId, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public List<Quiz> sortQuizByCreatedAt() {
        return quizRepository.findAllByOrderByCreatedAtDesc();
    }

    @Override
    public List<Quiz> sortQuizByPreference() {
        return quizRepository.findAllByOrderByPreferenceDesc();
    }

    @Override
    public List<Quiz> searchQuiz(String keyword) {
        return quizRepository.findByContentContaining(keyword);
    }

    @Override
    public void saveUserAnswer(UserAnswerDto userAnswer) {
        UserAnswer userAnswerEntity = UserAnswer.builder()
                .answerId(userAnswer.getAnswerId())
                .userId(userAnswer.getUserId())
                .quizId(userAnswer.getQuizId())
                .selectedOption(userAnswer.getSelectedOption())
                .answeredAt(userAnswer.getAnsweredAt())
                .timeSpent(userAnswer.getTimeSpent())
                .preference(userAnswer.getPreference())
                .build();
        userAnswerRepository.save(userAnswerEntity);
    }
}