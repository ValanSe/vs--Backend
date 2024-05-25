package com.valanse.valanse.controller;

import com.valanse.valanse.dto.NoticeRegisterDto;
import com.valanse.valanse.dto.StatusResponseDto;
import com.valanse.valanse.service.NoticeService.NoticeService;
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
@RequestMapping("/notice")
@Tag(name = "Notice Controller", description = "공지사항 관련 API를 관리합니다.")
public class NoticeController {

    private final NoticeService noticeService;

    @Operation(summary = "공지사항을 등록합니다.",
            description = "공지사항의 제목, 내용을 등록하여 새로운 공지사항을 등록합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "등록 성공", content = @Content(schema = @Schema(implementation = StatusResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 형식"),
            @ApiResponse(responseCode = "403", description = "해당 작업에 대한 권한 없음"),
            @ApiResponse(responseCode = "409", description = "필수 항목 불입력")
    })
    @PostMapping("/register")
    public ResponseEntity<StatusResponseDto> registerNotice(
            @Parameter(description = "HTTP 요청 객체", hidden = true)
            HttpServletRequest httpServletRequest,
            @Parameter(description = "공지사항 등록에 필요한 데이터", required = true, schema = @Schema(implementation = NoticeRegisterDto.class))
            @RequestPart NoticeRegisterDto noticeRegisterDto
    ) {
        noticeService.registerNotice(httpServletRequest, noticeRegisterDto);

        return ResponseEntity.ok(StatusResponseDto.success("notice register successfully"));
    }

    @Operation(summary = "특정 공지사항을 조회합니다.",
            description = "지정된 ID로 공지사항 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = StatusResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "해당 ID로 공지사항을 찾을 수 없음")
    })
    @GetMapping("/{noticeId}")
    public ResponseEntity<StatusResponseDto> getNotice(@PathVariable("noticeId") Integer noticeId) {
        return ResponseEntity.ok(StatusResponseDto.success(noticeService.getNotice(noticeId)));
    }

    @Operation(summary = "특정 공지사항을 갱신합니다.",
            description = "지정된 ID, 공지사항의 제목, 내용으로 공지사항을 갱신합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "갱신 성공", content = @Content(schema = @Schema(implementation = StatusResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 형식"),
            @ApiResponse(responseCode = "403", description = "해당 작업에 대한 권한 없음"),
            @ApiResponse(responseCode = "404", description = "해당 ID로 공지사항을 찾을 수 없음")
    })
    @PatchMapping("/{noticeId}")
    public ResponseEntity<StatusResponseDto> updateNotice(
            @Parameter(description = "HTTP 요청 객체", hidden = true)
            HttpServletRequest httpServletRequest,
            @PathVariable("noticeId") Integer noticeId,
            @Parameter(description = "공지사항 갱신에 필요한 데이터", required = true, schema = @Schema(implementation = NoticeRegisterDto.class))
            @RequestPart NoticeRegisterDto noticeRegisterDto
    ) {
        noticeService.updateNotice(httpServletRequest, noticeId, noticeRegisterDto);

        return ResponseEntity.ok(StatusResponseDto.success("notice updated successfully"));
    }

    @Operation(summary = "특정 공지사항을 삭제합니다.",
            description = "지정된 ID로 공지사항을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "삭제 성공", content = @Content(schema = @Schema(implementation = StatusResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "해당 작업에 대한 권한 없음"),
            @ApiResponse(responseCode = "404", description = "해당 ID로 공지사항을 찾을 수 없음")
    })
    @DeleteMapping("/{noticeId}")
    public ResponseEntity<StatusResponseDto> deleteNotice(
            @Parameter(description = "HTTP 요청 객체", hidden = true)
            HttpServletRequest httpServletRequest,
            @PathVariable("noticeId") Integer noticeId
    ) {
        noticeService.deleteNotice(httpServletRequest, noticeId);

        return ResponseEntity.ok(StatusResponseDto.success("notice deleted successfully"));
    }
}
