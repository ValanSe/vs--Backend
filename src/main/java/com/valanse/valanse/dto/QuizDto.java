package com.valanse.valanse.dto;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizDto {
    private Integer quizId; // 문제 식별자
    private Integer authorUserId; // 문제를 등록한 사용자 식별자
    private String content; // 문제 내용
    private String optionA; // 선택지 A
    private String optionB; // 선택지 B
    private String descriptionA; // 선택지 A 설명
    private String descriptionB; // 선택지 B 설명
    private String imageA; // 선택지 A 이미지 URL
    private String imageB; // 선택지 B 이미지 URL
    private Integer view; // 퀴즈의 조회수
    private Integer preference; // 퀴즈의 선호도 수
    private LocalDateTime createdAt; // 문제 생성 시간
    private LocalDateTime updatedAt; // 문제 수정 시간
}
