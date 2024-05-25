package com.valanse.valanse.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoticeDto {
    private Integer noticeId;

    private String title;

    private String content;

    private Integer authorId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Integer views;
}
