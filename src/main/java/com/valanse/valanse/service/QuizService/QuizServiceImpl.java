package com.valanse.valanse.service.QuizService;

import com.valanse.valanse.dto.*;
import com.valanse.valanse.entity.Quiz;
import com.valanse.valanse.entity.QuizCategory;
import com.valanse.valanse.entity.UserAnswer;
import com.valanse.valanse.repository.jpa.QuizCategoryRepository;
import com.valanse.valanse.repository.jpa.QuizRepository;
import com.valanse.valanse.repository.jpa.UserAnswerRepository;
import com.valanse.valanse.security.util.JwtUtil;
import com.valanse.valanse.service.ImageService.S3ImageService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuizServiceImpl implements QuizService {

    private final QuizRepository quizRepository;
    private final QuizCategoryRepository quizCategoryRepository;
    private final UserAnswerRepository userAnswerRepository;
    private final S3ImageService s3ImageService;
    private final JwtUtil jwtUtil;

    // 올바르지 못한 리턴 예시
    @Override
    public Quiz getRandomQuiz() {

        long count = quizRepository.count(); // 총 개수 구하기
        int randomIdx = new Random().nextInt((int) count + 1); // 랜덤 인덱스 생성
        return quizRepository.findById(randomIdx).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    @Transactional
    public QuizDto getQuiz(int quizId) {
        Quiz quiz = quizRepository.findById(quizId).orElseThrow(EntityNotFoundException::new);

        quizRepository.increaseViewCount(quiz.getQuizId());

        return QuizDto.builder()
                .quizId(quiz.getQuizId())
                .authorUserId(quiz.getAuthorUserId())
                .content(quiz.getContent())
                .optionA(quiz.getOptionA())
                .optionB(quiz.getOptionB())
                .descriptionA(quiz.getDescriptionA())
                .descriptionB(quiz.getDescriptionB())
                .view(quiz.getViewCount())
                .preference(quiz.getPreference())
                .likeCount(quiz.getLikeCount())
                .unlikeCount(quiz.getUnlikeCount())
                .createdAt(quiz.getCreatedAt())
                .updatedAt(quiz.getUpdatedAt())
                .build();
    }

    @Override
    @Transactional
    public void registerQuiz(HttpServletRequest httpServletRequest,
                             QuizRegisterDto quizRegisterDto,
                             MultipartFile image_A,
                             MultipartFile image_B) throws IOException {

        int userIdx = jwtUtil.getUserIdxFromRequest(httpServletRequest);

        String path_A = null;
        String path_B = null;

        if (image_A != null && !image_A.isEmpty()) {
            path_A = s3ImageService.uploadImage(image_A);
        }

        if (image_B != null && !image_B.isEmpty()) {
            path_B = s3ImageService.uploadImage(image_B);
        }

        Quiz quiz = Quiz.builder()
                .authorUserId(userIdx)
                .content(quizRegisterDto.getContent())
                .optionA(quizRegisterDto.getOptionA())
                .optionB(quizRegisterDto.getOptionB())
                .descriptionA(quizRegisterDto.getDescriptionA())
                .descriptionB(quizRegisterDto.getDescriptionB())
                .imageA(path_A)
                .imageB(path_B)
                .viewCount(0)
                .preference(0)
                .createdAt(LocalDateTime.now())
                .build();

        quizRepository.save(quiz); // 퀴즈 먼저 저장하여 ID를 생성

        List<String> categories = quizRegisterDto.getCategory() != null ? quizRegisterDto.getCategory() : new ArrayList<>();

        for (String category : categories) {

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
    public void updateQuiz(HttpServletRequest httpServletRequest, Integer quizId, QuizRegisterDto quizRegisterDto, MultipartFile image_A, MultipartFile image_B) throws IOException {
        try {
            int userIdx = jwtUtil.getUserIdxFromRequest(httpServletRequest);

            String imagePathA = null;
            String imagePathB = null;

            if (image_A != null && !image_A.isEmpty()) {
                imagePathA = s3ImageService.uploadImage(image_A);
            }

            if (image_B != null && !image_B.isEmpty()) {
                imagePathB = s3ImageService.uploadImage(image_B);
            }

            Quiz existingQuiz = quizRepository.findById(quizId).orElseThrow(EntityNotFoundException::new);

            if (existingQuiz.getAuthorUserId() != userIdx) {
                throw new AccessDeniedException("You don't have permission to update.");
            }

            existingQuiz = Quiz.builder()
                    .quizId(existingQuiz.getQuizId())
                    .authorUserId(existingQuiz.getAuthorUserId())
                    .content(quizRegisterDto.getContent() != null ? quizRegisterDto.getContent() : existingQuiz.getContent()) // db에서 NOT NULL이라 입력 값이 널이면 기존 db에 저장된 값
                    .optionA(quizRegisterDto.getOptionA() != null ? quizRegisterDto.getOptionA() : existingQuiz.getOptionA())
                    .optionB(quizRegisterDto.getOptionB() != null ? quizRegisterDto.getOptionB() : existingQuiz.getOptionB())
                    .descriptionA(quizRegisterDto.getDescriptionA()) // 널이어도 되므로 입력 값 그대로
                    .descriptionB(quizRegisterDto.getDescriptionB())
                    .imageA(imagePathA) // imagePath는 이미지가 널이 아니면 이미지의 경로, 널이면 널
                    .imageB(imagePathB)
                    .viewCount(existingQuiz.getViewCount())
                    .preference(existingQuiz.getPreference())
                    .createdAt(existingQuiz.getCreatedAt())
                    .updatedAt(LocalDateTime.now())
                    .build();

            quizRepository.save(existingQuiz);

            // 입력 카테고리 값이 널이 아니면 입력 값, 널이면 빈 리스트
            List<String> updatedCategory = quizRegisterDto.getCategory() != null ? quizRegisterDto.getCategory() : new ArrayList<>();

            quizCategoryRepository.deleteByQuizId(quizId); // quizId에 해당하는 quizId 컬럼과 기존에 매핑된 QuizCategory Entity 삭제

            // 입력 받은 카테고리 값과 quizId 매핑
            for (String category : updatedCategory) {
                QuizCategory quizCategory = QuizCategory.builder()
                        .category(category)
                        .quizId(quizId)
                        .build();

                quizCategoryRepository.save(quizCategory);
            }

        } catch (AccessDeniedException e) {
            log.error("Forbidden to update quiz with id {}", quizId, e);
            throw e;
        }
    }

    @Override
    @Transactional
    public void deleteQuiz(HttpServletRequest httpServletRequest, Integer quizId) {
        try {
            int userIdx = jwtUtil.getUserIdxFromRequest(httpServletRequest);

            Quiz quiz = quizRepository.findById(quizId).orElseThrow(EntityNotFoundException::new);

            if (quiz.getAuthorUserId() != userIdx) {
                throw new AccessDeniedException("You don't have permission to delete.");
            }

            quizRepository.delete(quiz);

        } catch (AccessDeniedException e) {
            log.error("Forbidden to delete quiz with id {}", quizId, e);
            throw e;
        }
    }

    @Override
    @Transactional
    public void increasePreference(Integer quizId) {
        try {
            Quiz quiz = quizRepository.findById(quizId).orElseThrow(EntityNotFoundException::new);

            quizRepository.increasePreference(quiz.getQuizId()); // 선호도 수 증가
            quizRepository.increaseLikeCount(quiz.getQuizId()); // 좋아요 수 증가
        } catch (EntityNotFoundException e) {
            log.error("Quiz not found with id {}", quizId, e);
            throw e;
        }
    }

    @Override
    @Transactional
    public void decreasePreference(Integer quizId) {
        try {
            Quiz quiz = quizRepository.findById(quizId).orElseThrow(EntityNotFoundException::new);

            quizRepository.decreasePreference(quiz.getQuizId()); // 선호도 수 감소
            quizRepository.increaseUnlikeCount(quiz.getQuizId()); // 싫어요 수 증가
        } catch (EntityNotFoundException e) {
            log.error("Quiz not found with id {}", quizId, e);
            throw e;
        }
    }

    @Override
    public QuizStatsDto getQuizStats(Integer quizId) {
        try {
            Quiz quiz = quizRepository.findById(quizId).orElseThrow(EntityNotFoundException::new);

            return QuizStatsDto.builder()
                    .viewsCount(quiz.getViewCount())
                    .preference(quiz.getPreference())
                    .build();
        } catch (EntityNotFoundException e) {
            log.error("Quiz not found with id {}", quizId, e);
            throw e;
        }
    }

    @Override
    public QuizLikeStatsDto getQuizLikeStats(Integer quizId) {
        try {
            Quiz quiz = quizRepository.findById(quizId).orElseThrow(EntityNotFoundException::new);

            return QuizLikeStatsDto.builder()
                    .likeCount(quiz.getLikeCount())
                    .unlikeCount(quiz.getUnlikeCount())
                    .build();
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
                .difficultyLevel(userAnswerDto.getDifficultyLevel())
                .build();

        userAnswerRepository.save(userAnswer);
    }
}
