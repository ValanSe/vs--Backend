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
    `view`           INT          NOT NULL COMMENT '조회수',
    `preference`     INT          NOT NULL COMMENT '선호도 수',
    `created_at`     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '질문 생성 시간',
    `updated_at`     DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '질문 수정 시간',
    FOREIGN KEY (`author_user_id`) REFERENCES `user` (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

CREATE TABLE `answer`
(
    `answer_id`        INT AUTO_INCREMENT PRIMARY KEY COMMENT '답변 식별자',
    `user_id`          INT      NOT NULL COMMENT '답변한 사용자 식별자',
    `quiz_id`          INT      NOT NULL COMMENT '답변한 질문 식별자',
    `selected_option`  ENUM('A', 'B') COMMENT '선택된 옵션 (시간 내 선택하지 못한 경우 NULL)',
    `answered_at`      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '답변 시간',
    `time_spent`       INT      NOT NULL COMMENT '답변에 걸린 시간(초)',
    `preference`       INT      NOT NULL COMMENT '문제에 대한 사용자의 호감도',
    `difficulty_level` INT      NOT NULL COMMENT '사용자가 선택한 문제의 난이도 (쉬움: 1, 보통: 2, 어려움: 3)',
    FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`),
    FOREIGN KEY (`quiz_id`) REFERENCES `quiz` (`quiz_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

CREATE TABLE `quiz_category`
(
    `quiz_id`     INT NOT NULL COMMENT '질문 식별자',
    `category` VARCHAR(100) NOT NULL COMMENT '카테고리',
    `category_id` INT NOT NULL COMMENT '카테고리 식별자',
    `quiz_id_list` INT NOT NULL COMMEnt '퀴즈 목록',
    PRIMARY KEY (`quiz_id`, `category_id`),
    FOREIGN KEY (`quiz_id`) REFERENCES `quiz` (`quiz_id`) ON DELETE CASCADE,
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4  
  COLLATE = utf8mb4_general_ci;