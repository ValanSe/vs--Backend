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

    private KafkaProducerService kafkaProducerService;

    @PostPersist
    @PostUpdate
    private void handleFavoriteCategoryChange(FavoriteCategory favoriteCategory) {
        try {
            // getBean을 사용하는 시점에 kafkaProducerService를 초기화합니다.
            if (kafkaProducerService == null) {
                kafkaProducerService = SpringContext.getBean(KafkaProducerService.class);
            }

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
