package com.valanse.valanse.service.CommentQuizService;

import com.valanse.valanse.dto.CommentQuizDto;

import java.util.List;

public interface CommentQuizService {
    List<CommentQuizDto> getCommentByQuiz(Integer quizId);
}
