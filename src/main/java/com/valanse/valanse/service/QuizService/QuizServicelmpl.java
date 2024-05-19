package com.valanse.valanse.service.QuizService;

import com.valanse.valanse.dto.UserAnswerDto;
import com.valanse.valanse.entity.Quiz;
import com.valanse.valanse.entity.UserAnswer;
import com.valanse.valanse.repository.jpa.QuizRepository;
import com.valanse.valanse.repository.jpa.UserAnswerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class QuizServicelmpl implements QuizService {

    private final QuizRepository quizRepository;
    private final UserAnswerRepository userAnswerRepository;

    @Override
    public void increaseView(Integer quizId) {
        try {
            Quiz quiz = quizRepository.findById(quizId).orElseThrow(EntityNotFoundException::new);
            quizRepository.increaseView(quiz.getQuizId()); // 조회수 증가
        } catch (EntityNotFoundException e) {
            log.error("Quiz not found with id {}", quizId, e);
            throw e;
        }
    }

    @Override
    public void increasePreference(Integer quizId) {
        try {
            Quiz quiz = quizRepository.findById(quizId).orElseThrow(EntityNotFoundException::new);
            quizRepository.increasePreference(quiz.getQuizId()); // 선호도 수 증가
        } catch (EntityNotFoundException e) {
            log.error("Quiz not found with id {}", quizId, e);
            throw e;
        }
    }

    @Override
    public void decreasePreference(Integer quizId) {
        try {
            Quiz quiz = quizRepository.findById(quizId).orElseThrow(EntityNotFoundException::new);
            quizRepository.decreasePreference(quiz.getQuizId()); // 선호도 수 감소
        } catch (EntityNotFoundException e) {
            log.error("Quiz not found with id {}", quizId, e);
            throw e;
        }
    }

    @Override
    public int getQuizPreference(Integer quizId) {
        try {
            Quiz quiz = quizRepository.findById(quizId).orElseThrow(EntityNotFoundException::new);
            return quiz.getPreference();
        } catch (EntityNotFoundException e) {
            log.error("Quiz not found with id {}", quizId, e);
            throw e;
        }
    }

    @Override
    public int getViewsCount(Integer quizId) {
        try {
            Quiz quiz = quizRepository.findById(quizId).orElseThrow(EntityNotFoundException::new);
            return quiz.getView();
        } catch (EntityNotFoundException e) {
            log.error("Quiz not found with id {}", quizId, e);
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
    public void saveUserAnswer(UserAnswerDto userAnswerDto) {
        UserAnswer userAnswer = UserAnswer.builder()
                .answerId(userAnswerDto.getAnswerId())
                .userId(userAnswerDto.getUserId())
                .quizId(userAnswerDto.getQuizId())
                .selectedOption(userAnswerDto.getSelectedOption())
                .answeredAt(userAnswerDto.getAnsweredAt())
                .timeSpent(userAnswerDto.getTimeSpent())
                .preference(userAnswerDto.getPreference())
                .build();
        userAnswerRepository.save(userAnswer);
    }
}