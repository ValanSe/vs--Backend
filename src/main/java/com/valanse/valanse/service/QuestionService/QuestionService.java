package com.valanse.valanse.service.QuestionService;

import com.valanse.valanse.dto.ClientResponseDto;
import com.valanse.valanse.entity.Question;
import com.valanse.valanse.entity.Category;

import java.util.List;
import java.util.Optional;

public interface QuestionService {
    void addQuestion(Question question); // 질문 등록
    void updateQuestion(Question question); // 질문 업데이트
    void deleteQuestion(Integer questionId); // 질문 삭제
    Optional<Question> findQuestionById(Integer questionId); // 질문 조회
    List<Question> findAllQuestions(); // 모든 질문 조회
    List<Question> findQuestionsByCategory(Category category); // 특정 카테고리의 질문 조회
    List<Question> provideQuestionToClient(); // 클라이언트에게 질문 제공
    void saveClientResponse(ClientResponseDto clientResponse); // 클라이언트의 답변을 데이터베이스에 저장
}