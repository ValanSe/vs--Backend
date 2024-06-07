package com.valanse.valanse.service.RecommendService;

import com.valanse.valanse.entity.FavoriteCategory;
import com.valanse.valanse.entity.RecommendQuiz;
import com.valanse.valanse.repository.jpa.FavoriteCategoryRepository;
import com.valanse.valanse.repository.jpa.RecommendQuizRepository;
import com.valanse.valanse.util.DataParseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class RecommendServiceImpl implements RecommendService {

    private final FavoriteCategoryRepository favoriteCategoryRepository;
    private final RecommendQuizRepository recommendQuizRepository;

    @Override
    public void updateFavoriteCategory(String data) {
        try {
            Map<String, String> dataMap = DataParseUtil.parseCommaSeparatedKeyValuePairs(data);
            Integer userId = Integer.valueOf(dataMap.get("userId"));
            String category = dataMap.get("category");

            FavoriteCategory newFavoriteCategory = FavoriteCategory.builder()
                    .userId(userId)
                    .category(category)
                    .build();

            favoriteCategoryRepository.save(newFavoriteCategory);
            log.info("Successfully updated favorite category for user_id: {}, category: {}", userId, category);
        } catch (NumberFormatException e) {
            log.error("Failed to parse userId from data: {}", data, e);
        } catch (Exception e) {
            log.error("An error occurred while updating favorite category with data: {}", data, e);
        }
    }

    @Override
    public void updateRecommendQuiz(String data) {
        try {
            Map<String, String> dataMap = DataParseUtil.parseCommaSeparatedKeyValuePairs(data);

            Integer userId = Integer.valueOf(dataMap.get("userId"));
            String recommendQuizIds = dataMap.get("recommendQuizIds");

            List<Integer> recommendQuizList = Arrays.stream(recommendQuizIds.split(","))
                    .map(Integer::valueOf)
                    .toList();

            List<RecommendQuiz> recommendQuizzes = recommendQuizList.stream()
                    .map(quizId -> RecommendQuiz.builder()
                            .userId(userId)
                            .quizId(quizId)
                            .build())
                    .toList();

            recommendQuizRepository.saveAll(recommendQuizzes);

        } catch (Exception e) {
            log.error("Error occurred while updating recommended quizzes: ", e);
        }
    }
}
