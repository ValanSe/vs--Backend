package com.valanse.valanse.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer noticeId;

    private String title;

    private String content;

    private Integer authorId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Integer views;
}
