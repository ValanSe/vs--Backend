package com.valanse.valanse.service.QuizCategoryService;

import com.valanse.valanse.dto.QuizCategoryDto;

import java.util.List;

public interface QuizCategoryService {

    List<QuizCategoryDto> getAllQuizByCategory(String category);

    List<QuizCategoryDto> searchCategory(String keyword); // 카테고리 검색
}