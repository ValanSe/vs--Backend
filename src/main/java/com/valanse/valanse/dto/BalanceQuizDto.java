package com.valanse.valanse.dto;


import lombok.Builder;

import java.time.LocalDateTime;


@Builder
public class BalanceQuizDto {
    private Integer quizId; // 문제 식별자
    private Integer authorUserId; // 문제를 등록한 사용자 식별자
    private String content; // 문제 내용
    private String optionA; // 선택지 A
    private String optionB; // 선택지 B
    private String descriptionA; // 선택지 A 설명
    private String descriptionB; // 선택지 B 설명
    private LocalDateTime createdAt; // 문제 생성 시간

}
