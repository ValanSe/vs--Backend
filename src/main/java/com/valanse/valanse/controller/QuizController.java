package com.valanse.valanse.controller;

import com.valanse.valanse.dto.QuizRegisterDto;
import com.valanse.valanse.dto.StatusResponseDto;
import com.valanse.valanse.service.BalanceProblemService.QuizService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/quiz")
public class QuizController {

    private final QuizService quizService;


    @GetMapping("/{quizId}")
    public ResponseEntity<StatusResponseDto> getQuiz(@PathVariable("quizId") Integer quizId) {
        return ResponseEntity.ok(StatusResponseDto.success(quizService.getQuiz(quizId)));
    }

    @PostMapping("/register")
    public ResponseEntity<StatusResponseDto> registerQuiz(
            HttpServletRequest httpServletRequest,
            @RequestPart QuizRegisterDto quizRegisterDto,
            @RequestPart MultipartFile image_A,
            @RequestPart MultipartFile image_B
    ) throws IOException {
        quizService.registerQuiz(httpServletRequest, quizRegisterDto, image_A, image_B);

        return ResponseEntity.ok(StatusResponseDto.success("quiz register success"));
    }
}
