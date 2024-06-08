package com.valanse.valanse.service.QuizCategoryService;

import com.valanse.valanse.dto.QuizCategoryDto;
import com.valanse.valanse.dto.QuizCategoryStatsDto;
import com.valanse.valanse.entity.QuizCategory;

import java.util.List;
import java.util.Optional;

public interface QuizCategoryService {

    List<QuizCategoryDto> getAllQuizByCategory(String category);

    List<QuizCategoryDto> searchCategory(String keyword); // 카테고리 검색

    QuizCategoryStatsDto getStatsByCategory(String category); // 카테고리에 속한 퀴즈 수, 카테고리에 속한 퀴즈의 조회수 합, 평균 선호도 수 조회
}