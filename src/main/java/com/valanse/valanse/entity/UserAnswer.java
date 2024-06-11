package com.valanse.valanse.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(UserAnswerId.class)
public class UserAnswer {
    @Id
    private Integer userId; // 답변한 사용자 식별자

    @Id
    private Integer quizId; // 답변한 퀴즈 식별자

    @Enumerated(EnumType.STRING)
    private OptionAB selectedOption; // 선택한 옵션
    private LocalDateTime answeredAt; // 답변한 시간
    private Integer preference; // 퀴즈에 대한 선호도
}
