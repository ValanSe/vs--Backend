package com.valanse.valanse.controller;

import com.valanse.valanse.dto.StatusResponseDto;
import com.valanse.valanse.service.QuizCategoryService.QuizCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/quiz-category")
@Tag(name = "Category Controller", description = "카테고리 관련 활동을 감지합니다.")
public class QuizCategoryController {

    private final QuizCategoryService quizCategoryService;

    @Operation(summary = "카테고리에 속한 퀴즈 조회",
            description = "카테고리에 속한 모든 퀴즈를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = StatusResponseDto.class)))
    })
    @GetMapping("/{category}")
    public ResponseEntity<StatusResponseDto> getAllQuizByCategory(@PathVariable String category) {
        return ResponseEntity.ok(StatusResponseDto.success(quizCategoryService.getAllQuizByCategory(category)));
    }

    @Operation(summary = "카테고리 검색",
            description = "지정된 키워드를 포함한 카테고리를 검색합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "검색 성공", content = @Content(schema = @Schema(implementation = StatusResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 형식")
    })
    @GetMapping("/search")
    public ResponseEntity<StatusResponseDto> searchCategory(
            @Parameter(description = "카테고리 검색을 위한 키워드")
            @RequestParam String keyword
    ) {
        return ResponseEntity.ok(StatusResponseDto.success(quizCategoryService.searchCategory(keyword)));
    }
}