package com.valanse.valanse.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
public class ClientResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer answerId; // 답변 식별자
    private Integer userId; // 답변한 사용자 식별자
    private Integer questionId; // 답변한 문제 식별자
    private String selectedOption; // 선택한 옵션
    private LocalDateTime answeredAt; // 답변한 시간
    private Integer timeSpent; // 답변에 소요된 시간 (초)
    private Integer preference; // 문제에 대한 선호도

    // private Integer difficultyLevel 사용자가 선택한 난이도
}