package com.valanse.valanse.kafka;

import com.valanse.valanse.service.RecommendService.RecommendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumerService {

    private static final String SERVER_NAME_CONSUME = "valanse-recommend";
    private final RecommendService recommendService;

    @KafkaListener(topics = SERVER_NAME_CONSUME)
    public void consume(String message) {
        String[] parts = message.split("\\|", 2);
        String eventType = parts[0];
        String data = parts[1];

        switch (eventType) {
            case EventTypes.FAVORITE_CATEGORY_CHANGED:
                handleFavoriteCategoryChanged(data);
                return;

            case EventTypes.RECOMMEND_QUIZ_CHANGED:
                handleRecommendQuizChange(data);
                return;

            default:
                log.warn("Unknown event type: {}", eventType);
                return;
        }
    }

    private void handleFavoriteCategoryChanged(String data) {
        try {
            // data 내용을 기반으로 최애 카테고리를 변경한다.
            recommendService.updateFavoriteCategory(data);

        } catch (Exception e) {
            log.error("Error handling favorite category change", e);

        }
    }

    private void handleRecommendQuizChange(String data) {
        try {
            // data 내용을 기반으로 추천 문제를 변경한다.
            recommendService.updateRecommendQuiz(data);

        } catch (Exception e) {
            log.error("Error handling recommend quiz change", e);
        }
    }

}