package com.valanse.valanse.service.CommentService;

import com.valanse.valanse.dto.CommentDto;
import com.valanse.valanse.dto.CommentQuizDto;
import com.valanse.valanse.dto.CommentRegisterDto;
import com.valanse.valanse.entity.Comment;
import com.valanse.valanse.entity.CommentQuiz;
import com.valanse.valanse.entity.Quiz;
import com.valanse.valanse.entity.QuizCategory;
import com.valanse.valanse.repository.jpa.*;
import com.valanse.valanse.security.util.JwtUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentQuizRepository commentQuizRepository;
    private final QuizRepository quizRepository;
    private final JwtUtil jwtUtil;
    private final UserCategoryPreferenceRepository userCategoryPreferenceRepository;
    private final QuizCategoryRepository quizCategoryRepository;


    @Override
    @Transactional
    public void registerComment(HttpServletRequest httpServletRequest, CommentRegisterDto commentRegisterDto) {
        try {
            int userIdx = jwtUtil.getUserIdxFromRequest(httpServletRequest);

            Quiz quiz = quizRepository.findById(commentRegisterDto.getQuizId())
                    .orElseThrow(() -> new EntityNotFoundException("Quiz not found with id: " + commentRegisterDto.getQuizId()));

            String content = commentRegisterDto.getContent();

            Comment comment = Comment.builder()
                    .authorUserId(userIdx)
                    .content(content)
                    .createdAt(LocalDateTime.now())
                    .build();

            commentRepository.save(comment);

            CommentQuiz commentQuiz = CommentQuiz.builder()
                    .quizId(quiz.getQuizId())
                    .commentId(comment.getCommentId())
                    .build();

            commentQuizRepository.save(commentQuiz);

            List<QuizCategory> quizCategories = quizCategoryRepository.findByQuizId(quiz.getQuizId());
            List<String> categories = quizCategories.stream()
                    .map(QuizCategory::getCategory)
                    .collect(Collectors.toList());

            userCategoryPreferenceRepository.incrementCommentCounts(userIdx, categories);

        } catch (EntityNotFoundException e) {
            log.error("Entity not found when registering comment for quiz id: {}", commentRegisterDto.getQuizId(), e);
            throw e;
        } catch (Exception e) {
            log.error("An unexpected error occurred while registering comment for quiz id: {}", commentRegisterDto.getQuizId(), e);
            throw e;
        }
    }


    @Override
    public CommentDto getComment(Integer commentId) {

        Comment comment = commentRepository.findById(commentId).orElseThrow(EntityNotFoundException::new);

        return CommentDto.builder()
                .commentId(comment.getCommentId())
                .authorUserId(comment.getAuthorUserId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }

    @Override
    public List<CommentQuizDto> getCommentByQuizId(Integer quizId) {

        return commentQuizRepository.findByQuizId(quizId).stream()
                .map(commentQuiz -> CommentQuizDto.builder()
                        .quizId(commentQuiz.getQuizId())
                        .commentId(commentQuiz.getCommentId())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateComment(HttpServletRequest httpServletRequest, Integer commentId, String content) {
        int userIdx = 0;

        try {
            userIdx = jwtUtil.getUserIdxFromRequest(httpServletRequest);

            Comment existingComment = commentRepository.findById(commentId).orElseThrow(EntityNotFoundException::new);

            if (!existingComment.getAuthorUserId().equals(userIdx)) {
                throw new AccessDeniedException("You don't have permission to update.");
            }

            existingComment = Comment.builder()
                    .commentId(existingComment.getCommentId())
                    .authorUserId(existingComment.getAuthorUserId())
                    .content(content != null ? content : existingComment.getContent())
                    .createdAt(existingComment.getCreatedAt())
                    .updatedAt(LocalDateTime.now())
                    .build();

            commentRepository.save(existingComment);

        } catch (AccessDeniedException e) {
            log.error("Access denied when updating comment with id: {} by user: {}", commentId, userIdx);
            throw e;
        } catch (EntityNotFoundException e) {
            log.error("Entity not found when updating comment with id: {}", commentId);
            throw e;
        } catch (Exception e) {
            log.error("An unexpected error occurred while updating comment with id: {}", commentId, e);
            throw e;
        }
    }

    @Override
    @Transactional
    public void deleteComment(HttpServletRequest httpServletRequest, Integer commentId) {
        try {
            int userIdx = jwtUtil.getUserIdxFromRequest(httpServletRequest);

            Comment comment = commentRepository.findById(commentId).orElseThrow(EntityNotFoundException::new);

            if (comment.getAuthorUserId() != userIdx) {
                throw new AccessDeniedException("You don't have permission to delete");
            }

            commentRepository.delete(comment);

        } catch (AccessDeniedException e) {
            log.error("You don't have permission to delete");
            throw e;
        }
    }
}
