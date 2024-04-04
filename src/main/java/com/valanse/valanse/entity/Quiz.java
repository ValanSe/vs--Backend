package com.valanse.valanse.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer quizId; // 퀴즈 식별자
    private Integer authorUserId; // 퀴즈를 등록한 사용자 식별자
    private String content; // 퀴즈 내용
    private String optionA; // 선택지 A
    private String optionB; // 선택지 B
    private String descriptionA; // 선택지 A 설명
    private String descriptionB; // 선택지 B 설명
    private LocalDateTime createdAt; // 퀴즈 생성 시간
}