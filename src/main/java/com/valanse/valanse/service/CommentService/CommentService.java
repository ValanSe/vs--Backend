package com.valanse.valanse.service.CommentService;

import com.valanse.valanse.dto.CommentDto;
import com.valanse.valanse.dto.CommentRegisterDto;
import com.valanse.valanse.dto.CommentUpdateDto;
import jakarta.servlet.http.HttpServletRequest;

public interface CommentService {
    void registerComment(HttpServletRequest httpServletRequest, CommentRegisterDto commentRegisterDto);

    CommentDto getComment(Integer commentId);

    void updateComment(HttpServletRequest httpServletRequest, Integer commentId, CommentUpdateDto commentUpdateDto);

    void deleteComment(HttpServletRequest httpServletRequest, Integer commentId);
}
