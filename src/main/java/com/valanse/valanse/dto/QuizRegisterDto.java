package com.valanse.valanse.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizRegisterDto {

    private String content; // 문제 내용
    private String optionA; // 선택지 A
    private String optionB; // 선택지 B
    private String descriptionA; // 선택지 A 설명
    private String descriptionB; // 선택지 B 설명
    private List<String> category; // 카테고리

}
