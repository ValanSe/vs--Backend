package com.valanse.valanse.service.BalanceProblemService;

import com.valanse.valanse.dto.QuizDto;
import com.valanse.valanse.dto.QuizRegisterDto;
import com.valanse.valanse.entity.Quiz;
import com.valanse.valanse.entity.QuizCategory;
import com.valanse.valanse.entity.UserAnswer;
import com.valanse.valanse.repository.jpa.QuizCategoryRepository;
import com.valanse.valanse.repository.jpa.QuizRepository;
import com.valanse.valanse.security.util.JwtUtil;
import com.valanse.valanse.util.FileUploadUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service("balanceProblemQuizService")
@RequiredArgsConstructor
@Slf4j
public class QuizServiceImpl implements QuizService {

    private final QuizRepository quizRepository;
    private final QuizCategoryRepository quizCategoryRepository;
    private final FileUploadUtil fileUploadUtil;
    private final JwtUtil jwtUtil;


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

    @Override
    @Transactional
    public void registerQuiz(HttpServletRequest httpServletRequest,
                             QuizRegisterDto quizRegisterDto,
                             MultipartFile image_A,
                             MultipartFile image_B) throws IOException {

        int userIdx = jwtUtil.getUserIdxFromRequest(httpServletRequest);

        String path_A = fileUploadUtil.saveFile(image_A);
        String path_B = fileUploadUtil.saveFile(image_B);

        Quiz quiz = Quiz.builder()
                .authorUserId(userIdx)
                .content(quizRegisterDto.getContent())
                .optionA(quizRegisterDto.getOptionA())
                .optionB(quizRegisterDto.getOptionB())
                .descriptionA(quizRegisterDto.getDescriptionA())
                .descriptionB(quizRegisterDto.getDescriptionB())
                .imageA(path_A)
                .imageB(path_B)
                .view(0)
                .view(0)
                .preference(0)
                .createdAt(LocalDateTime.now())
                .build();

        quizRepository.save(quiz); // 퀴즈 먼저 저장하여 ID를 생성

        for (String category : quizRegisterDto.getCategory()) {

            if (category == null || category.trim().isEmpty()) {
                continue; // 무효한 카테고리는 건너뛴다
            }

            QuizCategory quizCategory = QuizCategory.builder()
                    .category(category)
                    .quizId(quiz.getQuizId()) // 생성된 퀴즈 ID를 사용
                    .build();

            quizCategoryRepository.save(quizCategory); // 퀴즈 카테고리 저장
        }


    }

    @Override
    @Transactional
    public void deleteQuiz(HttpServletRequest httpServletRequest, int quizId) {

        int userIdx = jwtUtil.getUserIdxFromRequest(httpServletRequest);

        Quiz quiz = quizRepository.findById(quizId).orElseThrow(EntityNotFoundException::new);

        if (quiz.getAuthorUserId() != userIdx) {
            throw new UnauthorizedException("You don't have permission to delete this quiz.");
        }

        List<QuizCategory> categories = quizCategoryRepository.findByQuizId(quizId);

        for(QuizCategory category : categories) {
            quizRepository.deleteById(quizId);
            quizCategoryRepository.save(category);
        }

        quizRepository.delete(quiz);
    }

}
