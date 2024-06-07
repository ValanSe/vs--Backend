package com.valanse.valanse.entity;

import com.valanse.valanse.kafka.listener.FavoriteCategoryListener;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(FavoriteCategoryListener.class)
public class FavoriteCategory {

    @Id
    private Integer userId;

    private String category;
}
