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
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentQuizRepository commentQuizRepository;
    private final QuizRepository quizRepository;
    private final JwtUtil jwtUtil;

    private static final Set<String> profanityWords = Set.of(
            "비속어 목록"
    );

    @Override
    public void registerComment(HttpServletRequest httpServletRequest, CommentRegisterDto commentRegisterDto) {

        int userIdx = jwtUtil.getUserIdxFromRequest(httpServletRequest);

        Quiz quiz = quizRepository.findById(commentRegisterDto.getQuizId())
                .orElseThrow(() -> new InvalidDataAccessApiUsageException("Quiz not found with id: " + commentRegisterDto.getQuizId()));

        String content = commentRegisterDto.getContent();

        for (String profanityWord : profanityWords) {
            String regex = "(?i)" + String.join("[^\\p{IsHangul}]*", profanityWord.split("")); // 비속어를 문자 단위로 분할하고 한글 이외의 문자가 있어도 대소문자 무시하고 매칭
            Pattern pattern = Pattern.compile(regex); // 생성된 정규식을 사용하여 Pattern 객체 생성
            Matcher matcher = pattern.matcher(content); // 주어진 댓글에 대해 정규식 매칭을 수행할 Matcher 객체 생성
            StringBuffer sb = new StringBuffer(); // 변경된 댓글을 저장할 StringBuffer 객체 생성

            // 정규식 매칭을 통해 댓글에서 비속어를 찾고 해당 비속어를 *로 대체
            while (matcher.find()) {
                matcher.appendReplacement(sb, "*".repeat(profanityWord.length()));
            }

            matcher.appendTail(sb); // 변경된 부분을 StringBuffer에 추가
            content = sb.toString(); // 변경된 댓글을 content에 저장
        }

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
        try {
            int userIdx = jwtUtil.getUserIdxFromRequest(httpServletRequest);

            Comment existingComment = commentRepository.findById(commentId).orElseThrow(EntityNotFoundException::new);

            if (existingComment.getAuthorUserId() != userIdx) {
                throw new AccessDeniedException("You don't have permission to update.");
            }

            String content = commentUpdateDto.getContent();

            for (String profanityWord : profanityWords) {
                String regex = "(?i)" + String.join("[^\\p{IsHangul}]*", profanityWord.split(""));
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(content);
                StringBuffer sb = new StringBuffer();

                while (matcher.find()) {
                    matcher.appendReplacement(sb, "*".repeat(profanityWord.length()));
                }

                matcher.appendTail(sb);
                content = sb.toString();
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
            log.error("You don't have permission to update.");
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
