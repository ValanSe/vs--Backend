package com.valanse.valanse.controller;

import com.valanse.valanse.dto.CommentRegisterDto;
import com.valanse.valanse.dto.StatusResponseDto;
import com.valanse.valanse.service.CommentService.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
@Tag(name = "Comment Controller", description = "댓글 관련 API를 관리합니다.")
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "댓글을 등록합니다.",
            description = "댓글의 내용과 해당하는 퀴즈의 ID를 입력하여 새로운 댓글을 등록합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "등록 성공", content = @Content(schema = @Schema(implementation = StatusResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 형식"),
            @ApiResponse(responseCode = "409", description = "필수 항목 누락")
    })
    @PostMapping("/register")
    public ResponseEntity<StatusResponseDto> registerComment(
            @Parameter(description = "HTTP 요청 객체", hidden = true)
            HttpServletRequest httpServletRequest,
            @Parameter(description = "댓글 등록에 필요한 데이터", required = true, schema = @Schema(implementation = CommentRegisterDto.class))
            @RequestBody CommentRegisterDto commentRegisterDto
    ) {
        commentService.registerComment(httpServletRequest, commentRegisterDto);

        return ResponseEntity.ok(StatusResponseDto.success("comment registered successfully"));
    }

    @Operation(summary = "특정 댓글을 조회합니다.",
            description = "지정된 ID로 댓글 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = StatusResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "해당 ID로 댓글을 찾을 수 없음")
    })
    @GetMapping("/{commentId}")
    public ResponseEntity<StatusResponseDto> getComment(@PathVariable("commentId") Integer commentId) {
        return ResponseEntity.ok(StatusResponseDto.success(commentService.getComment(commentId)));
    }

    @Operation(summary = "특정 퀴즈의 댓글을 조회합니다.",
            description = "지정된 ID의 퀴즈의 댓글 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = StatusResponseDto.class)))
    })
    @GetMapping("/quiz/{quizId}")
    public ResponseEntity<StatusResponseDto> getCommentByQuizId(@PathVariable Integer quizId) {
        return ResponseEntity.ok(StatusResponseDto.success(commentService.getCommentByQuizId(quizId)));
    }

    @Operation(summary = "특정 댓글을 갱신합니다.",
            description = "입력 받은 댓글의 내용으로 댓글을 갱신합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "갱신 성공", content = @Content(schema = @Schema(implementation = StatusResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 형식"),
            @ApiResponse(responseCode = "403", description = "해당 작업에 대한 권한 없음"),
            @ApiResponse(responseCode = "404", description = "해당 ID로 댓글을 찾을 수 없음")
    })
    @PatchMapping("/{commentId}")
    public ResponseEntity<StatusResponseDto> updateComment(
            @Parameter(description = "HTTP 요청 객체", hidden = true)
            HttpServletRequest httpServletRequest,
            @PathVariable("commentId") Integer commentId,
            @Parameter(description = "수정할 댓글 내용")
            @RequestParam String content
    ) {
        commentService.updateComment(httpServletRequest, commentId, content);

        return ResponseEntity.ok(StatusResponseDto.success("comment updated successfully"));
    }

    @Operation(summary = "특정 댓글을 삭제합니다.",
            description = "지정된 ID로 댓글을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "삭제 성공", content = @Content(schema = @Schema(implementation = StatusResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "해당 작업에 대한 권한 없음"),
            @ApiResponse(responseCode = "404", description = "해당 ID로 댓글을 찾을 수 없음")
    })
    @DeleteMapping("/{commentId}")
    public ResponseEntity<StatusResponseDto> deleteComment(
            @Parameter(description = "HTTP 요청 객체", hidden = true)
            HttpServletRequest httpServletRequest,
            @PathVariable("commentId") Integer commentId
    ) {
        commentService.deleteComment(httpServletRequest, commentId);

        return ResponseEntity.ok(StatusResponseDto.success("comment deleted successfully"));
    }
}
