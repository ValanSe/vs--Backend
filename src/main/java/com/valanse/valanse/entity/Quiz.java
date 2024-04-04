package com.valanse.valanse.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer quizId; // 문제 식별자
    private Integer authorUserId; // 문제를 등록한 사용자 식별자
    private String content; // 문제 내용
    @Column(name = "option_a")
    private String optionA; // 선택지 A
    @Column(name = "option_b")
    private String optionB; // 선택지 B
    @Column(name = "description_a")
    private String descriptionA; // 선택지 A 설명
    @Column(name = "description_b")
    private String descriptionB; // 선택지 B 설명
    private LocalDateTime createdAt; // 문제 생성 시간
}