package com.valanse.valanse.service.BalanceProblemService;

import com.valanse.valanse.dto.QuizDto;
import com.valanse.valanse.entity.Quiz;
import com.valanse.valanse.entity.UserAnswer;
import com.valanse.valanse.repository.QuizRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service("balanceProblemQuizService")
@RequiredArgsConstructor
@Slf4j
public class QuizServiceImpl implements QuizService {

    private final QuizRepository quizRepository;


    // 올바르지 못한 리턴 예시
    @Override
    public Quiz getRandomQuiz() {

        long count = quizRepository.count(); // 총 개수 구하기
        int randomIdx = new Random().nextInt((int) count + 1); // 랜덤 인덱스 생성
        return quizRepository.findById(randomIdx).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public QuizDto getQuiz(int quizId) {
        Quiz quiz = quizRepository.findById(quizId).orElseThrow(EntityNotFoundException::new);

        return QuizDto.builder()
                .quizId(quiz.getQuizId())
                .authorUserId(quiz.getAuthorUserId())
                .content(quiz.getContent())
                .optionA(quiz.getOptionA())
                .optionB(quiz.getOptionB())
                .descriptionA(quiz.getDescriptionA())
                .descriptionB(quiz.getDescriptionB())
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Override
    public void saveUserAnswer(UserAnswer userAnswer) {


    }


}
