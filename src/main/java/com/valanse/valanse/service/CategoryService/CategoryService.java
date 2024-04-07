package com.valanse.valanse.service.CategoryService;

import com.valanse.valanse.entity.Category;
import com.valanse.valanse.entity.Quiz;

public interface CategoryService {
    void createCategory(Category category); // 카테고리 생성
    void updateCategory(Category category); // 카테고리 업데이트
    void deleteCategory(Integer categoryId); // 카테고리 삭제
    void addQuestionToCategory(Category category, Quiz quiz); // 카테고리에 질문 추가
    void removeQuestionFromCategory(Category category, Quiz quiz); // 카테고리에서 질문 제거

    // 데이터베이스에서 질문이 갱신될 시 카테고리에 있는 질문도 갱신
    void updateQuestionsInCategory(Category category, Quiz updatedQuiz);

    // 데이터베이스에서 질문이 삭제 시 카테고리에 있는 질문도 삭제
    void deleteQuestionFromCategory(Category category, Integer questionId);
}
