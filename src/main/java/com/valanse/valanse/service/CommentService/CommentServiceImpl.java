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
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

//    private static final Set<String> profanityWords = Set.of(
//            "비속어 목록"
//    );

    @Override
    @Transactional
    public void registerComment(HttpServletRequest httpServletRequest, CommentRegisterDto commentRegisterDto) {
        try {
            int userIdx = jwtUtil.getUserIdxFromRequest(httpServletRequest);

            Quiz quiz = quizRepository.findById(commentRegisterDto.getQuizId())
                    .orElseThrow(() -> new EntityNotFoundException("Quiz not found with id: " + commentRegisterDto.getQuizId()));

            String content = commentRegisterDto.getContent();

//        for (String profanityWord : profanityWords) {
//            String regex = "(?i)" + String.join("[^\\p{IsHangul}]*", profanityWord.split("")); // 비속어를 문자 단위로 분할하고 한글 이외의 문자가 있어도 대소문자 무시하고 매칭
//            Pattern pattern = Pattern.compile(regex); // 생성된 정규식을 사용하여 Pattern 객체 생성
//            Matcher matcher = pattern.matcher(content); // 주어진 댓글에 대해 정규식 매칭을 수행할 Matcher 객체 생성
//            StringBuffer sb = new StringBuffer(); // 변경된 댓글을 저장할 StringBuffer 객체 생성
//
//            // 정규식 매칭을 통해 댓글에서 비속어를 찾고 해당 비속어를 *로 대체
//            while (matcher.find()) {
//                matcher.appendReplacement(sb, "*".repeat(profanityWord.length()));
//            }
//
//            matcher.appendTail(sb); // 변경된 부분을 StringBuffer에 추가
//            content = sb.toString(); // 변경된 댓글을 content에 저장
//        }

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

//            for (String profanityWord : profanityWords) {
//                String regex = "(?i)" + String.join("[^\\p{IsHangul}]*", profanityWord.split(""));
//                Pattern pattern = Pattern.compile(regex);
//                Matcher matcher = pattern.matcher(content);
//                StringBuffer sb = new StringBuffer();
//
//                while (matcher.find()) {
//                    matcher.appendReplacement(sb, "*".repeat(profanityWord.length()));
//                }
//
//                matcher.appendTail(sb);
//                content = sb.toString();
//            }

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

            if (!comment.getAuthorUserId().equals(userIdx)) {
                throw new AccessDeniedException("You don't have permission to delete");
            }

            commentRepository.delete(comment);

        } catch (AccessDeniedException e) {
            log.error("Forbidden to delete comment with id {}", commentId, e);
            throw e;
        }
    }
}
