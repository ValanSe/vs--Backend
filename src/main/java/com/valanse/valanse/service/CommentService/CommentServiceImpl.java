package com.valanse.valanse.service.CommentService;

import com.valanse.valanse.dto.CommentDto;
import com.valanse.valanse.dto.CommentRegisterDto;
import com.valanse.valanse.dto.CommentUpdateDto;
import com.valanse.valanse.entity.Comment;
import com.valanse.valanse.entity.CommentQuiz;
import com.valanse.valanse.entity.Quiz;
import com.valanse.valanse.repository.jpa.CommentQuizRepository;
import com.valanse.valanse.repository.jpa.CommentRepository;
import com.valanse.valanse.repository.jpa.QuizRepository;
import com.valanse.valanse.security.util.JwtUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentQuizRepository commentQuizRepository;
    private final QuizRepository quizRepository;
    private final JwtUtil jwtUtil;

    @Override
    public void registerComment(HttpServletRequest httpServletRequest, CommentRegisterDto commentRegisterDto) {

        int userIdx = jwtUtil.getUserIdxFromRequest(httpServletRequest);

        Quiz quiz = quizRepository.findById(commentRegisterDto.getQuizId())
                .orElseThrow(() -> new InvalidDataAccessApiUsageException("Quiz not found with id: " + commentRegisterDto.getQuizId()));

        Comment comment = Comment.builder()
                .authorUserId(userIdx)
                .content(commentRegisterDto.getContent())
                .createdAt(LocalDateTime.now())
                .build();

        commentRepository.save(comment);

        CommentQuiz commentQuiz = CommentQuiz.builder()
                .quizId(quiz.getQuizId())
                .commentId(comment.getCommentId())
                .build();

        commentQuizRepository.save(commentQuiz);

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
    @Transactional
    public void updateComment(HttpServletRequest httpServletRequest, Integer commentId, CommentUpdateDto commentUpdateDto) {

        int userIdx = jwtUtil.getUserIdxFromRequest(httpServletRequest);

        Comment existingComment = commentRepository.findById(commentId).orElseThrow(EntityNotFoundException::new);

        if (existingComment.getAuthorUserId() != userIdx) {
            throw new AccessDeniedException("You don't have permission to update.");
        }

        existingComment = Comment.builder()
                .commentId(existingComment.getCommentId())
                .authorUserId(existingComment.getAuthorUserId())
                .content(commentUpdateDto.getContent() != null ? commentUpdateDto.getContent() : existingComment.getContent())
                .createdAt(existingComment.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();

        commentRepository.save(existingComment);
    }

    @Override
    @Transactional
    public void deleteComment(HttpServletRequest httpServletRequest, Integer commentId) {

        int userIdx = jwtUtil.getUserIdxFromRequest(httpServletRequest);

        Comment comment = commentRepository.findById(commentId).orElseThrow(EntityNotFoundException::new);

        if (comment.getAuthorUserId() != userIdx) {
            throw new AccessDeniedException("You don't have permission to delete");
        }

        commentRepository.delete(comment);
    }
}
