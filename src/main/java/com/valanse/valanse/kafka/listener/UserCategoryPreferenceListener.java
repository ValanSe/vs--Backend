package com.valanse.valanse.kafka.listener;

import com.valanse.valanse.config.SpringContext;
import com.valanse.valanse.entity.UserCategoryPreference;
import com.valanse.valanse.kafka.EventTypes;
import com.valanse.valanse.kafka.KafkaProducerService;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostUpdate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserCategoryPreferenceListener {

    private KafkaProducerService kafkaProducerService;

    @PostPersist
    @PostUpdate
    private void handleUserCategoryPreferenceChange(UserCategoryPreference userCategoryPreference) {
        // getBean을 사용하는 시점에 kafkaProducerService를 초기화합니다.
        if (kafkaProducerService == null) {
            kafkaProducerService = SpringContext.getBean(KafkaProducerService.class);
        }

        try {
            String data = String.format(
                    "userId:%d,category:%s",
                    userCategoryPreference.getUserId(),
                    userCategoryPreference.getCategory()
            );
            kafkaProducerService.sendChangeEvent(EventTypes.USER_CATEGORY_PREFERENCE_CHANGED, data);
            log.info("Processed UserCategoryPreference change for user_id: {}, category: {}",
                    userCategoryPreference.getUserId(),
                    userCategoryPreference.getCategory());
        } catch (Exception e) {
            log.error("Error processing UserCategoryPreference change", e);
        }
    }
}
