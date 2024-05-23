package com.valanse.valanse.service.QuizCategoryService;

import com.valanse.valanse.entity.QuizCategory;

import java.util.List;
import java.util.Optional;

public interface QuizCategoryService {

    List<QuizCategory> searchCategory(String keyword); // 카테고리 검색

    int getQuizCountByCategory(String category); // 카테고리에 속한 퀴즈 수 조회

    double getAveragePreferenceByCategory(String category); // 카테고리에 속한 퀴즈의 평균 선호도 조회

    int getViewsCountByCategory(String category); // 카테고리에 속한 퀴즈의 조회수 조회
}
