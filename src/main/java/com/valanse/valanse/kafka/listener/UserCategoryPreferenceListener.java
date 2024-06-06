package com.valanse.valanse.kafka.listener;

import com.valanse.valanse.config.SpringContext;
import com.valanse.valanse.entity.UserCategoryPreference;
import com.valanse.valanse.kafka.EventTypes;
import com.valanse.valanse.kafka.KafkaProducerService;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostUpdate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserCategoryPreferenceListener {

    // JPA 엔티티 리스너 -> 스프링 컨텍스트 외부 -> Spring Bean으로 직접 등록될 수 없음
    private KafkaProducerService kafkaProducerService;

    public UserCategoryPreferenceListener() {
        // 보관해둔 SpringContext -> KafkaProducerService Bean 주입
        this.kafkaProducerService = SpringContext.getBean(KafkaProducerService.class);
    }

    @PostPersist
    @PostUpdate
    private void handleUserCategoryPreferenceChange(UserCategoryPreference userCategoryPreference) {
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
