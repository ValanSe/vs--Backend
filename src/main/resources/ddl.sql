CREATE TABLE `naver_user`
(
    `id`         VARCHAR(255) PRIMARY KEY COMMENT '네이버 사용자 ID',
    `email`      VARCHAR(100) COMMENT '이메일 주소',
    `name`       VARCHAR(100) COMMENT '사용자 이름',
    `birth_year` VARCHAR(100) COMMENT '출생년도'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

CREATE TABLE `google_user`
(
    `id`    VARCHAR(255) PRIMARY KEY COMMENT '구글 사용자 고유 ID',
    `email` VARCHAR(100) COMMENT '이메일 주소',
    `name`  VARCHAR(100) COMMENT '사용자 이름'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

CREATE TABLE `kakao_user`
(
    `id`   VARCHAR(255) PRIMARY KEY COMMENT '카카오 사용자 고유 ID',
    `name` VARCHAR(100) COMMENT '사용자 이름'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

CREATE TABLE `user`
(
    `user_id`        INT AUTO_INCREMENT PRIMARY KEY COMMENT '사용자 식별자',
    `oauth_provider` VARCHAR(50)  NOT NULL COMMENT 'oauth 제공자',
    `oauth_id`       VARCHAR(255) NOT NULL COMMENT 'oauth 고유 ID',
    `role`           VARCHAR(50)  NOT NULL COMMENT '사용자 역할',
    `status`         VARCHAR(50)  NOT NULL COMMENT '계정 상태',
    `created_at`     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '가입 시간'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

CREATE TABLE `quiz`
(
    `quiz_id`        INT AUTO_INCREMENT PRIMARY KEY COMMENT '질문 식별자',
    `author_user_id` INT          NOT NULL COMMENT '질문을 등록한 사용자 식별자',
    `content`        TEXT         NOT NULL COMMENT '질문 내용',
    `option_a`       VARCHAR(255) NOT NULL COMMENT '선택지 A',
    `option_b`       VARCHAR(255) NOT NULL COMMENT '선택지 B',
    `description_a`  TEXT COMMENT 'A 설명',
    `description_b`  TEXT COMMENT 'B 설명',
    `image_a`        TEXT COMMENT 'A 이미지',
    `image_b`        TEXT COMMENT 'B 이미지',
    `view_count`     INT          NOT NULL COMMENT '조회수',
    `preference`     INT          NOT NULL COMMENT '선호도',
    `like_count`     INT          NOT NULL COMMENT '좋아요 수',
    `unlike_count`   INT          NOT NULL COMMENT '싫어요 수',
    `created_at`     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '퀴즈 생성 시간',
    `updated_at`     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '퀴즈 수정 시간',
    FOREIGN KEY (`author_user_id`) REFERENCES `user` (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

CREATE TABLE `user_answer`
(
    `user_id`         INT      NOT NULL COMMENT '답변한 사용자 식별자',
    `quiz_id`         INT      NOT NULL COMMENT '답변한 퀴즈 식별자',
    `selected_option` VARCHAR(255) COMMENT '선택된 옵션',
    `answered_at`     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '답변 시간',
    `preference`      INT      NOT NULL COMMENT '문제에 대한 사용자의 호감도',
    `like_count`      INT      NOT NULL COMMENT '퀴즈의 좋아요 수',
    `unlike_count`    INT      NOT NULL COMMENT '퀴즈의 싫어요 수',
    PRIMARY KEY (`user_id`, `quiz_id`),
    FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`),
    FOREIGN KEY (`quiz_id`) REFERENCES `quiz` (`quiz_id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

CREATE TABLE `quiz_category`
(
    `quiz_id`  INT          NOT NULL COMMENT '퀴즈 식별자',
    `category` VARCHAR(100) NOT NULL COMMENT '카테고리 식별자',
    PRIMARY KEY (`quiz_id`, `category`),
    FOREIGN KEY (`quiz_id`) REFERENCES `quiz` (`quiz_id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

CREATE TABLE `notice`
(
    notice_id  INT AUTO_INCREMENT PRIMARY KEY COMMENT '공지사항 식별자',
    title      VARCHAR(255) NOT NULL COMMENT '제목',
    content    TEXT         NOT NULL COMMENT '내용',
    author_id  INT          NOT NULL COMMENT '공지사항을 등록한 관리자 식별자',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '공지사항 생성 시간',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '공지사항 수정 시간',
    views      INT      DEFAULT 0 COMMENT '조회수',
    FOREIGN KEY (author_id) REFERENCES `user` (user_id) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

CREATE TABLE comment
(
    comment_id     INT AUTO_INCREMENT PRIMARY KEY COMMENT '댓글 식별자',
    author_user_id INT          NOT NULL COMMENT '댓글 작성 식별자',
    content        VARCHAR(255) NOT NULL COMMENT '댓글 내용',
    created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '댓글 생성 시간',
    updated_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '댓글 수정 시간'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

CREATE TABLE `comment_quiz`
(
    `quiz_id`    INT NOT NULL COMMENT '퀴즈 식별자',
    `comment_id` INT NOT NULL COMMENT '댓글 식별자',
    PRIMARY KEY (`quiz_id`, `comment_id`),
    FOREIGN KEY (`quiz_id`) REFERENCES `quiz` (`quiz_id`) ON DELETE CASCADE,
    FOREIGN KEY (`comment_id`) REFERENCES `comment` (`comment_id`) ON DELETE CASCADE
) ENGINE = InnoDB
DEFAULT CHARSET = utf8mb4
COLLATE = utf8mb4_general_ci;

CREATE TABLE user_category_preference
(
    user_id            INT          NOT NULL COMMENT '사용자 식별자',
    category           VARCHAR(100) NOT NULL COMMENT '카테고리 식별자',
    answer_count       INT          NOT NULL DEFAULT 0 COMMENT '해당 카테고리에서 사용자가 푼 문제 수',
    total_preference   INT          NOT NULL DEFAULT 0 COMMENT '해당 카테고리에서 사용자의 푼 문제의 선호도 합',
    comment_count      INT          NOT NULL DEFAULT 0 COMMENT '해당 카테고리에서 사용자가 등록한 댓글 수',
    registration_count INT          NOT NULL DEFAULT 0 COMMENT '해당 카테고리에서 사용자가 등록한 문제 수',
    recommend_score    INT          NOT NULL DEFAULT 0 COMMENT '해당 카테고리 추천 점수',
    PRIMARY KEY (user_id, category),
    FOREIGN KEY (user_id) REFERENCES user (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE category_statistics
(
    category       VARCHAR(100) NOT NULL COMMENT '카테고리 식별자',
    total_answers  INT          NOT NULL DEFAULT 0 COMMENT '해당 카테고리에서 전체 사용자가 푼 문제 수',
    total_score    INT          NOT NULL DEFAULT 0 COMMENT '해당 카테고리에서 전체 사용자의 총 선호도 점수',
    avg_preference FLOAT AS (total_score / total_answers) STORED COMMENT '해당 카테고리에서 전체 사용자의 평균 선호도',
    PRIMARY KEY (category)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE favorite_category
(
    user_id  INT AUTO_INCREMENT NOT NULL COMMENT '사용자 식별자',
    category VARCHAR(100) NOT NULL COMMENT '카테고리 식별자',
    PRIMARY KEY (user_id),
    FOREIGN KEY (user_id) REFERENCES user (user_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE recommend_quiz
(
    user_id INT NOT NULL COMMENT '사용자 식별자',
    quiz_id INT NOT NULL COMMENT '퀴즈 식별자',
    PRIMARY KEY (user_id, quiz_id),
    FOREIGN KEY (user_id) REFERENCES user (user_id) ON DELETE CASCADE,
    FOREIGN KEY (quiz_id) REFERENCES quiz (quiz_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
