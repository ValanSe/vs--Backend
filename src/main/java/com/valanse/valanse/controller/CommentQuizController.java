package com.valanse.valanse.controller;

import com.valanse.valanse.dto.StatusResponseDto;
import com.valanse.valanse.service.CommentQuizService.CommentQuizService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/comment-quiz")
@Tag(name = "Comment Quiz Controller", description = "퀴즈 댓글 관련 API를 관리합니다.")
public class CommentQuizController {

    private final CommentQuizService commentQuizService;

    @Operation(summary = "특정 퀴즈의 댓글을 조회합니다.",
            description = "지정된 ID의 퀴즈의 댓글 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = StatusResponseDto.class)))
    })
    @GetMapping("/{quizId}")
    public ResponseEntity<StatusResponseDto> getCommentByQuiz(@PathVariable Integer quizId) {
        return ResponseEntity.ok(StatusResponseDto.success(commentQuizService.getCommentByQuiz(quizId)));
    }
}
