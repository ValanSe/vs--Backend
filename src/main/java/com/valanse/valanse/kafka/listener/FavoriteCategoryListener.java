package com.valanse.valanse.kafka.listener;

import com.valanse.valanse.config.SpringContext;
import com.valanse.valanse.entity.FavoriteCategory;
import com.valanse.valanse.kafka.EventTypes;
import com.valanse.valanse.kafka.KafkaProducerService;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostUpdate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FavoriteCategoryListener {

    // JPA 엔티티 리스너 -> 스프링 컨텍스트 외부 -> Spring Bean으로 직접 등록될 수 없음
    private KafkaProducerService kafkaProducerService;

    public FavoriteCategoryListener() {
        // 보관해둔 SpringContext -> KafkaProducerService Bean 주입
        this.kafkaProducerService = SpringContext.getBean(KafkaProducerService.class);
    }

    @PostPersist
    @PostUpdate
    private void handleFavoriteCategoryChange(FavoriteCategory favoriteCategory) {
        try {
            String data = String.format(
                    "userId:%d,category:%s",
                    favoriteCategory.getUserId(),
                    favoriteCategory.getCategory()
            );
            kafkaProducerService.sendChangeEvent(EventTypes.FAVORITE_CATEGORY_CHANGED, data);

            log.info("Processed FavoriteCategory change for user_id: {}, category: {}",
                    favoriteCategory.getUserId(),
                    favoriteCategory.getCategory());
        } catch (Exception e) {
            log.error("Error processing FavoriteCategory change", e);
        }
    }
}
