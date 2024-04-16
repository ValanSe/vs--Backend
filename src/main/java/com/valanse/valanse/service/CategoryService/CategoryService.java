package com.valanse.valanse.service.CategoryService;

import com.valanse.valanse.entity.Category;
import com.valanse.valanse.entity.Quiz;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    void addQuizToCategory(Category category, Quiz quiz); // 카테고리에 질문 추가
    void removeQuizFromCategory(Category category, Quiz quiz); // 카테고리에서 질문 제거
    List<Category> searchCategory(String keyword); // 카테고리 검색
    Optional<Integer> findQuizCountByCategory(Category category); // 카테고리에 속한 퀴즈 수 조회
    Optional<Double> findAveragePreferenceByCategory(Category category); // 카테고리에 속한 퀴즈의 평균 선호도 조회
    Optional<Integer> findRecentActivityByCategory(Category category); // 카테고리에 속한 퀴즈의 최근 활동(댓글 수, 조회수 등) 조회
}
