package com.valanse.valanse.service.BalanceProblemService;

import com.valanse.valanse.entity.Quiz;
import com.valanse.valanse.entity.UserAnswer;
import com.valanse.valanse.repository.jpa.QuizRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuizServiceImpl implements QuizService {

    private final QuizRepository quizRepository;

    @Override
    public Quiz getRandomQuiz() {

        long count = quizRepository.count(); // 총 개수 구하기
        int randomIdx = new Random().nextInt((int) count + 1); // 랜덤 인덱스 생성
        return quizRepository.findById(randomIdx).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public void saveUserAnswer(UserAnswer userAnswer) {


    }


}
