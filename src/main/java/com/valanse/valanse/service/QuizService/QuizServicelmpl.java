package com.valanse.valanse.service.QuizService;

import com.valanse.valanse.dto.QuizDto;
import com.valanse.valanse.dto.UserAnswerDto;
import com.valanse.valanse.entity.Quiz;
import com.valanse.valanse.entity.UserAnswer;
import com.valanse.valanse.repository.jpa.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuizServicelmpl implements QuizService {

    @Autowired
    private final QuizRepository quizRepository;

    @Override
    public QuizDto getQuiz(Integer quizId) {
        Optional<Quiz> optionalQuiz = quizRepository.findById(quizId);
        if(optionalQuiz.isPresent()) {
            Quiz quiz = optionalQuiz.get();
            quiz.incrementViews(); // 조회수 증가
            quizRepository.save(quiz);
            return QuizDto.builder()
                    .quizId(quiz.getQuizId())
                    .authorUserId(quiz.getAuthorUserId())
                    .content(quiz.getContent())
                    .optionA(quiz.getOptionA())
                    .optionB(quiz.getOptionB())
                    .descriptionA(quiz.getDescriptionA())
                    .descriptionB(quiz.getDescriptionB())
                    .createdAt(quiz.getCreatedAt())
                    .build();
        } else {
            throw new QuizNotFoundException("Quiz not found with id: " + quizId);
        }
    }

    @Override
    public void increasePreference(Integer quizId, int preference) {
        Optional<Quiz> optionalQuiz = quizRepository.findById(quizId);
        if(optionalQuiz.isPresent()) {
            Quiz quiz = optionalQuiz.get();
            quiz.incrementPreference(preference); // 선호도 수 증가
            quizRepository.save(quiz);
        } else {
            throw new QuizNotFoundException("Quiz not found with id: " + quizId);
        }
    }

    @Override
    public void increaseComment(Integer quizId) {
        Optional<Quiz> optionalQuiz = quizRepository.findById(quizId);
        if(optionalQuiz.isPresent()) {
            Quiz quiz = optionalQuiz.get();
            quiz.incrementComments(); // 댓글 수 증가
            quizRepository.save(quiz);
        } else {
            throw new QuizNotFoundException("Quiz not found with id: " + quizId);
        }
    }

    @Override
    public Optional<Integer> getQuizPreference(Integer quizId) {
        Optional<Quiz> optionalQuiz = quizRepository.findById(quizId);
        if(optionalQuiz.isPresent()) {
            Quiz quiz = optionalQuiz.get();
            return Optional.ofNullable(quiz.getPreference());
        } else {
            throw new QuizNotFoundException("Quiz not found with id: " + quizId);
        }
    }

    @Override
    public Optional<Integer> getViewsCount(Integer quizId) {
        Optional<Quiz> optionalQuiz = quizRepository.findById(quizId);
        if(optionalQuiz.isPresent()) {
            Quiz quiz = optionalQuiz.get();
            return Optional.ofNullable(quiz.getView());
        } else {
            throw new QuizNotFoundException("Quiz not found with id: " + quizId);
        }
    }

    @Override
    public Optional<Integer> getCommentsCount(Integer quizId) {
        Optional<Quiz> optionalQuiz = quizRepository.findById(quizId);
        if(optionalQuiz.isPresent()) {
            Quiz quiz = optionalQuiz.get();
            return Optional.ofNullable(quiz.getComment());
        } else {
            throw new QuizNotFoundException("Quiz not found with id: " + quizId);
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

    }
}
