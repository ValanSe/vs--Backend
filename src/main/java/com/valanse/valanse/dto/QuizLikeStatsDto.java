package com.valanse.valanse.dto;

import lombok.*;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizLikeStatsDto {
    private Integer likeCount;

    private Integer unlikeCount;
}
