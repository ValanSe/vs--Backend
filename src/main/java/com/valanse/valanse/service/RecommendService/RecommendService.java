package com.valanse.valanse.service.RecommendService;

import com.valanse.valanse.entity.Quiz;

import java.util.List;

public interface RecommendService {
    List<Quiz> recommendByPreference(int recommendQuizCount); // 선호도가 가장 높은 문제들을 추천하는 메서드
    List<Quiz> recommendByViews(int recommendQuizCount); // 조회수가 가장 높은 문제들을 추천하는 메서드
}