package com.valanse.valanse.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAnswerDto {
    private Integer answerId; // 답변 식별자
    private Integer userId; // 답변한 사용자 식별자
    private Integer quizId; // 답변한 퀴즈 식별자
    private String selectedOption; // 선택한 옵션
    private LocalDateTime answeredAt; // 답변한 시간
    private Integer preference; // 퀴즈에 대한 선호도
    private String status;
}
