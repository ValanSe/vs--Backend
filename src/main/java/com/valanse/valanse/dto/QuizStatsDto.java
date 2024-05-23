package com.valanse.valanse.dto;

import lombok.*;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizStatsDto {
    private int viewsCount;

    private int preference;
}
