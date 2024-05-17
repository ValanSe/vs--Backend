package com.valanse.valanse.controller;

import com.valanse.valanse.dto.QuizRegisterDto;
import com.valanse.valanse.dto.StatusResponseDto;
import com.valanse.valanse.service.BalanceProblemService.ForbiddenException;
import com.valanse.valanse.service.BalanceProblemService.QuizService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/quiz")
@Tag(name = "Quiz Controller", description = "퀴즈 관련 API를 관리합니다")
public class QuizController {

    private final QuizService quizService;

    @Operation(summary = "특정 퀴즈를 조회합니다.",
            description = "지정된 ID로 퀴즈 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = StatusResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "해당 ID로 퀴즈를 찾을 수 없음")
    })
    @GetMapping("/{quizId}")
    public ResponseEntity<StatusResponseDto> getQuiz(@PathVariable("quizId") Integer quizId) {
        return ResponseEntity.ok(StatusResponseDto.success(quizService.getQuiz(quizId)));
    }

    @Operation(summary = "새로운 퀴즈를 등록합니다.",
            description = "퀴즈의 내용, 옵션, 이미지를 등록하여 새로운 퀴즈를 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "등록 성공", content = @Content(schema = @Schema(implementation = StatusResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 형식"),
            @ApiResponse(responseCode = "500", description = "서버 오류로 인해 퀴즈 등록 실패")
    })
    @PostMapping("/register")
    public ResponseEntity<StatusResponseDto> registerQuiz(
            @Parameter(description = "HTTP 요청 객체", hidden = true)
            HttpServletRequest httpServletRequest,
            @Parameter(description = "퀴즈 등록에 필요한 데이터", required = true, schema = @Schema(implementation = QuizRegisterDto.class))
            @RequestPart QuizRegisterDto quizRegisterDto,
            @Parameter(description = "옵션 A에 대한 이미지", required = true)
            @RequestPart MultipartFile image_A,
            @Parameter(description = "옵션 B에 대한 이미지", required = true)
            @RequestPart MultipartFile image_B
    ) throws IOException {
        quizService.registerQuiz(httpServletRequest, quizRegisterDto, image_A, image_B);

        return ResponseEntity.ok(StatusResponseDto.success("quiz register success"));
    }

    @Operation(summary = "특정 퀴즈를 갱신합니다.",
            description = "지정된 ID, 퀴즈의 내용, 옵션, 이미지로 퀴즈를 갱신합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "갱신 성공", content = @Content(schema = @Schema(implementation = StatusResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "해당 작업에 대한 권한 없음"),
            @ApiResponse(responseCode = "404", description = "해당 ID로 퀴즈를 찾을 수 없음")
    })
    @PatchMapping("/patch/{quizId}")
    public ResponseEntity<StatusResponseDto> updateQuiz(
            @Parameter(description = "HTTP 요청 객체", hidden = true)
            HttpServletRequest httpServletRequest,
            @PathVariable("quizId") Integer quizId,
            @Parameter(description = "퀴즈 갱신에 필요한 데이터", schema = @Schema(implementation = QuizController.class))
            @RequestPart QuizRegisterDto quizRegisterDto,
            @Parameter(description = "옵션 A에 대한 이미지")
            @RequestPart MultipartFile image_A,
            @Parameter(description = "옵션 B에 대한 이미지")
            @RequestPart MultipartFile image_B
    ) throws IOException {
        try {
            quizService.updateQuiz(httpServletRequest, quizId, quizRegisterDto, image_A, image_B);

            return ResponseEntity.ok(StatusResponseDto.success("quiz updated successfully"));
        } catch (ForbiddenException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(StatusResponseDto.error(HttpStatus.FORBIDDEN.value(), e.getMessage()));
        }
    }

    @Operation(summary = "특정 퀴즈를 삭제합니다.",
            description = "지정된 ID로 퀴즈를 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "삭제 성공", content = @Content(schema = @Schema(implementation = StatusResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "해당 작업에 대한 권한 없음"),
            @ApiResponse(responseCode = "404", description = "해당 ID로 퀴즈를 찾을 수 없음")
    })
    @DeleteMapping("/delete/{quizId}")
    public ResponseEntity<StatusResponseDto> deleteQuiz(
            @Parameter(description = "HTTP 요청 객체", hidden = true)
            HttpServletRequest httpServletRequest,
            @PathVariable("quizId") Integer quizId
    ) {
        try {
            quizService.deleteQuiz(httpServletRequest, quizId);

            return ResponseEntity.ok(StatusResponseDto.success("Quiz deleted successfully"));
        } catch (ForbiddenException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(StatusResponseDto.error(HttpStatus.FORBIDDEN.value(), e.getMessage()));
        }
    }
}
