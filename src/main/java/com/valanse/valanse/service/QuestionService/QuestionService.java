package com.valanse.valanse.service.QuestionService;

import com.valanse.valanse.entity.ClientResponse;
import com.valanse.valanse.entity.Question;
import com.valanse.valanse.entity.Category;

import java.util.List;

public interface QuestionService {
    void addQuestion(Question question); // 질문 등록
    void updateQuestion(Question question); // 질문 업데이트
    void deleteQuestion(Integer questionId); // 질문 삭제
    Question getQuestionById(Integer questionId); // 질문 조회
    List<Question> getAllQuestions(); // 모든 질문 조회
    List<Question> getQuestionsByCategory(Category category); // 특정 카테고리의 질문 조회
    List<Question> provideQuestionToClient(); // 클라이언트에게 질문 제공
    void saveClientResponse(ClientResponse clientResponse); // 클라이언트의 답변을 데이터베이스에 저장
}
