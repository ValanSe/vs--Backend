package com.valanse.valanse.service.CommentService;

import com.valanse.valanse.dto.CommentDto;
import com.valanse.valanse.dto.CommentQuizDto;
import com.valanse.valanse.dto.CommentRegisterDto;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface CommentService {
    void registerComment(HttpServletRequest httpServletRequest, CommentRegisterDto commentRegisterDto);

    CommentDto getComment(Integer commentId);

    List<CommentQuizDto> getCommentByQuizId(Integer quizId);

    void updateComment(HttpServletRequest httpServletRequest, Integer commentId, String content);

    void deleteComment(HttpServletRequest httpServletRequest, Integer commentId);
}
