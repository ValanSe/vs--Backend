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

    private Integer view; // 퀴즈의 조회수
    private Integer comment; // 퀴즈의 댓글 수
    private Integer preference; // 퀴즈의 선호도 수

    private LocalDateTime createdAt; // 퀴즈 생성 시간

    // 조회수 증가
    public void incrementViews() {
        this.view++;
    }

    // 댓글 수 증가
    public void incrementComments() {
        this.comment++;
    }

    // 선호도 수 증가
    public void incrementPreference(Integer preference) {
        this.preference += preference;
    }
}