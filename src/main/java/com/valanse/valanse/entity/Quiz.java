package com.valanse.valanse.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer quizId; // 퀴즈 식별자
    private Integer authorUserId; // 퀴즈를 등록한 사용자 식별자
    private String content; // 퀴즈 내용
    @Column(name = "option_a")
    private String optionA; // 선택지 A
    @Column(name = "option_b")
    private String optionB; // 선택지 B
    @Column(name = "description_a")
    private String descriptionA; // 선택지 A 설명
    @Column(name = "description_b")
    private String descriptionB; // 선택지 B 설명
    @Column(name = "image_a")
    private String imageA; // 선택지 A 이미지
    @Column(name = "image_b")
    private String imageB; // 선택지 B 이미지

    private Integer viewCount; // 조회수
    private Integer preference; // 선호도 수

    private LocalDateTime createdAt; // 퀴즈 생성 시간
    private LocalDateTime updatedAt; // 퀴즈 수정 시간

}