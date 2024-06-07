package com.valanse.valanse.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAnswerDto {
    private Integer userId; // 답변한 사용자 식별자
    private Integer quizId; // 답변한 퀴즈 식별자
    private String selectedOption; // 선택한 옵션
    private Integer preference; // 퀴즈에 대한 선호도
    private Integer likeCount; // 퀴즈의 좋아요 수
    private Integer unlikeCount; // 퀴즈의 싫어요 수
}
