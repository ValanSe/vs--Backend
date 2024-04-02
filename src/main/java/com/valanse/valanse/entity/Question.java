package com.valanse.valanse.entity;
import com.valanse.valanse.entity.CategoryQuestion;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer questionId; // 문제 식별자
    private Integer authorUserId; // 문제를 등록한 사용자 식별자
    private String content; // 문제 내용
    private String optionA; // 선택지 A
    private String optionB; // 선택지 B
    private String descriptionA; // 선택지 A 설명
    private String descriptionB; // 선택지 B 설명
    private LocalDateTime createdAt; // 문제 생성 시간
    @OneToMany(mappedBy = "question")
    private List<CategoryQuestion> categoryQuestions;
}