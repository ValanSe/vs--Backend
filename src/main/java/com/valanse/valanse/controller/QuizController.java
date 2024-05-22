package com.valanse.valanse.controller;

import com.valanse.valanse.dto.QuizRegisterDto;
import com.valanse.valanse.dto.StatusResponseDto;
import com.valanse.valanse.dto.UserAnswerDto;
import com.valanse.valanse.entity.Quiz;
import com.valanse.valanse.service.QuizService.QuizService;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

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
            @ApiResponse(responseCode = "400", description = "잘못된 요청 형식"),
            @ApiResponse(responseCode = "403", description = "해당 작업에 대한 권한 없음"),
            @ApiResponse(responseCode = "404", description = "해당 ID로 퀴즈를 찾을 수 없음")
    })
    @PatchMapping("/{quizId}")
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
        quizService.updateQuiz(httpServletRequest, quizId, quizRegisterDto, image_A, image_B);

        return ResponseEntity.ok(StatusResponseDto.success("quiz updated successfully"));
    }

    @Operation(summary = "특정 퀴즈를 삭제합니다.",
            description = "지정된 ID로 퀴즈를 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "삭제 성공", content = @Content(schema = @Schema(implementation = StatusResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "해당 작업에 대한 권한 없음"),
            @ApiResponse(responseCode = "404", description = "해당 ID로 퀴즈를 찾을 수 없음")
    })
    @DeleteMapping("/{quizId}")
    public ResponseEntity<StatusResponseDto> deleteQuiz(
            @Parameter(description = "HTTP 요청 객체", hidden = true)
            HttpServletRequest httpServletRequest,
            @PathVariable("quizId") Integer quizId
    ) {
        quizService.deleteQuiz(httpServletRequest, quizId);

        return ResponseEntity.ok(StatusResponseDto.success("Quiz deleted successfully"));
    }

    @Operation(summary = "특정 퀴즈 선호도 증가",
            description = "지정된 ID의 퀴즈의 선호도를 증가합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "선호도 증가 성공", content = @Content(schema = @Schema(implementation = StatusResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "해당 ID로 퀴즈를 찾을 수 없음")
    })
    @PostMapping("/{quizId}/increase-preference")
    public ResponseEntity<StatusResponseDto> increasePreference(@PathVariable Integer quizId) {
        quizService.increasePreference(quizId);

        return ResponseEntity.ok(StatusResponseDto.success("Preference increased successfully"));
    }

    @Operation(summary = "특정 퀴즈 선호도 감소",
            description = "지정된 ID의 퀴즈의 선호도를 감소합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "선호도 감소 성공", content = @Content(schema = @Schema(implementation = StatusResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "해당 ID로 퀴즈를 찾을 수 없음")
    })
    @PostMapping("/{quizId}/decrease-preference")
    public ResponseEntity<StatusResponseDto> decreasePreference(@PathVariable Integer quizId) {
        quizService.decreasePreference(quizId);

        return ResponseEntity.ok(StatusResponseDto.success("Preference decreased successfully"));
    }

    @Operation(summary = "특정 퀴즈 선호도 조회",
            description = "지정된 ID의 퀴즈의 선호도를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "선호도 조회 성공", content = @Content(schema = @Schema(implementation = StatusResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "해당 ID로 퀴즈를 찾을 수 없음")
    })
    @GetMapping("/{quizId}/preference")
    public ResponseEntity<StatusResponseDto> getQuizPreference(@PathVariable Integer quizId) {
        return ResponseEntity.ok(StatusResponseDto.success(quizService.getQuizPreference(quizId)));
    }

    @Operation(summary = "특정 퀴즈 조회수 조회",
            description = "지정된 ID의 퀴즈의 조회수를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회수 조회 성공", content = @Content(schema = @Schema(implementation = StatusResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "해당 ID로 퀴즈를 찾을 수 없음")
    })
    @GetMapping("/{quizId}/views")
    public ResponseEntity<StatusResponseDto> getViewsCount(@PathVariable Integer quizId) {
        return ResponseEntity.ok(StatusResponseDto.success(quizService.getViewsCount(quizId)));
    }

    @Operation(summary = "퀴즈 생성 시간 순 정렬",
            description = "퀴즈를 생성 시간이 최신인 순으로 정렬")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정렬 성공", content = @Content(schema = @Schema(implementation = StatusResponseDto.class))),
    })
    @GetMapping("/sort-by-created-at")
    public ResponseEntity<StatusResponseDto> sortQuizByCreatedAt() {
        List<Quiz> quizzes = quizService.sortQuizByCreatedAt();

        return ResponseEntity.ok(StatusResponseDto.success(quizzes));
    }

    @Operation(summary = "퀴즈 선호도 순 정렬",
            description = "퀴즈를 선호도가 높은 순으로 정렬")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정렬 성공", content = @Content(schema = @Schema(implementation = StatusResponseDto.class))),
    })
    @GetMapping("/sort-by-preference")
    public ResponseEntity<StatusResponseDto> sortQuizByPreference() {
        List<Quiz> quizzes = quizService.sortQuizByPreference();

        return ResponseEntity.ok(StatusResponseDto.success(quizzes));
    }

    @Operation(summary = "퀴즈 검색",
            description = "지정된 키워드를 포함하는 퀴즈 검색")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "검색 성공", content = @Content(schema = @Schema(implementation = StatusResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 형식")
    })
    @GetMapping("/search")
    public ResponseEntity<StatusResponseDto> searchQuiz(
            @Parameter(description = "퀴즈 검색을 위한 키워드", required = false)
            @RequestParam String keyword
    ) {
        List<Quiz> quizzes = quizService.searchQuiz(keyword);

        return ResponseEntity.ok(StatusResponseDto.success(quizzes));
    }

    @Operation(summary = "사용자의 답변 저장",
            description = "사용자의 답변을 데이터베이스에 저장")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "저장 성공", content = @Content(schema = @Schema(implementation = StatusResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 형식")
    })
    @PostMapping("/save-user-answer")
    public ResponseEntity<StatusResponseDto> saveUserAnswer(
            @Parameter(description = "사용자의 답변", required = true, schema = @Schema(implementation = UserAnswerDto.class))
            @RequestPart UserAnswerDto userAnswerDto
    ) {
        quizService.saveUserAnswer(userAnswerDto);

        return ResponseEntity.ok(StatusResponseDto.success("User answer saved successfully"));
    }
}
