package com.valanse.valanse.service.QuizService;

import com.valanse.valanse.dto.QuizDto;
import com.valanse.valanse.dto.QuizRegisterDto;
import com.valanse.valanse.dto.UserAnswerDto;
import com.valanse.valanse.entity.Quiz;
import com.valanse.valanse.entity.QuizCategory;
import com.valanse.valanse.repository.jpa.QuizCategoryRepository;
import com.valanse.valanse.repository.jpa.QuizRepository;
import com.valanse.valanse.security.util.JwtUtil;
import com.valanse.valanse.service.ImageService.S3ImageService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuizServiceImpl implements QuizService {

    private final QuizRepository quizRepository;
    private final QuizCategoryRepository quizCategoryRepository;
    private final S3ImageService s3ImageService;
    private final JwtUtil jwtUtil;


    @Override
    public QuizDto getQuiz(Integer quizId) {
        try {
            Quiz quiz = quizRepository.findById(quizId)
                    .orElseThrow(() -> new EntityNotFoundException("Quiz not found with id: " + quizId));
            return QuizDto.builder()
                    .quizId(quiz.getQuizId())
                    .authorUserId(quiz.getAuthorUserId())
                    .content(quiz.getContent())
                    .optionA(quiz.getOptionA())
                    .optionB(quiz.getOptionB())
                    .descriptionA(quiz.getDescriptionA())
                    .descriptionB(quiz.getDescriptionB())
                    .imageA(quiz.getImageA())
                    .imageB(quiz.getImageB())
                    .createdAt(quiz.getCreatedAt())
                    .updatedAt(quiz.getUpdatedAt())
                    .build();
        } catch (Exception e) {
            log.error("Error retrieving quiz with id {}: {}", quizId, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void increasePreference(Integer quizId, int preference) {
        try {
            quizRepository.findById(quizId)
                    .orElseThrow(() -> new EntityNotFoundException("Quiz not found with id: " + quizId));
            quizRepository.increasePreference(quizId, preference); // 선호도 수 증가
        } catch (Exception e) {
            log.error("Error retrieving quiz with id {}: {}", quizId, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public Optional<Integer> getQuizPreference(Integer quizId) {
        try {
            return quizRepository.findById(quizId)
                    .map(Quiz::getPreference)
                    .orElseThrow(() -> new EntityNotFoundException("Quiz not found with id: " + quizId)).describeConstable();
        } catch (Exception e) {
            log.error("Error retrieving quiz with id {}: {}", quizId, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public Optional<Integer> getViewsCount(Integer quizId) {
        try {
            return quizRepository.findById(quizId)
                    .map(Quiz::getView)
                    .orElseThrow(() -> new EntityNotFoundException("Quiz not found with id: " + quizId)).describeConstable();
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

    }

    @Override
    @Transactional
    public void registerQuiz(HttpServletRequest httpServletRequest,
                             QuizRegisterDto quizRegisterDto,
                             MultipartFile image_A,
                             MultipartFile image_B) throws IOException {

        int userIdx = jwtUtil.getUserIdxFromRequest(httpServletRequest);

        String path_A = s3ImageService.uploadImage(image_A);
        String path_B = s3ImageService.uploadImage(image_B);

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
}
