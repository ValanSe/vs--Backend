package com.valanse.valanse.service.CommentQuizService;

import com.valanse.valanse.dto.CommentQuizDto;
import com.valanse.valanse.repository.jpa.CommentQuizRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentQuizServiceImpl implements CommentQuizService {

    private final CommentQuizRepository commentQuizRepository;

    @Override
    public List<CommentQuizDto> getCommentByQuiz(Integer quizId) {
        return commentQuizRepository.findByQuizId(quizId).stream()
                .map(commentQuiz -> CommentQuizDto.builder()
                        .quizId(commentQuiz.getQuizId())
                        .commentId(commentQuiz.getCommentId())
                        .build())
                .collect(Collectors.toList());
    }
}
