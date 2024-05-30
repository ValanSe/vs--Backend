package com.valanse.valanse.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDto {
    private Integer commentId;

    private Integer authorUserId;

    private String content;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
