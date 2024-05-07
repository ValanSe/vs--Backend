package com.valanse.valanse.service.QuizCategoryService;

import com.valanse.valanse.entity.Quiz;
import com.valanse.valanse.entity.QuizCategory;

import java.util.List;
import java.util.Optional;

public interface QuizCategoryService {
    void addQuizToCategory(QuizCategory category, Quiz quiz) throws IllegalAccessException; // 카테고리에 질문 추가
    List<QuizCategory> searchCategory(String keyword); // 카테고리 검색
    Optional<Integer> getQuizCountByCategory(QuizCategory category); // 카테고리에 속한 퀴즈 수 조회
    Optional<Double> getAveragePreferenceByCategory(QuizCategory category); // 카테고리에 속한 퀴즈의 평균 선호도 조회
    Optional<Integer> getViewsCountByCategory(QuizCategory category); // 카테고리에 속한 퀴즈의 조회수 조회
}
