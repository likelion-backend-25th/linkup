DROP TABLE IF EXISTS report;
DROP TABLE IF EXISTS notification;
DROP TABLE IF EXISTS follow;
DROP TABLE IF EXISTS block;
DROP TABLE IF EXISTS payment;
DROP TABLE IF EXISTS subscription;
DROP TABLE IF EXISTS reply_like;
DROP TABLE IF EXISTS reply;
DROP TABLE IF EXISTS post_like;
DROP TABLE IF EXISTS post_image;
DROP TABLE IF EXISTS post;
DROP TABLE IF EXISTS member;


-- =========================================================
-- 1. member (회원 기본)
-- =========================================================
CREATE TABLE member (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NULL,
    name VARCHAR(50) NOT NULL,
    unique_id VARCHAR(30) NOT NULL UNIQUE,
    profile_image VARCHAR(255) NULL,
    introduction TEXT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'ROLE_USER',
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    warning_count INT NOT NULL DEFAULT 0,
    writing_restricted_until DATETIME NULL,
    following_count INT NOT NULL DEFAULT 0,
    follower_count INT NOT NULL DEFAULT 0,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP
);


-- =========================================================
-- 2. post (피드 게시글)
-- =========================================================
CREATE TABLE post (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT NOT NULL,
    content TEXT NOT NULL,
    file_url VARCHAR(255) NULL,
    like_count INT NOT NULL DEFAULT 0,
    subscriber_only BOOLEAN NOT NULL DEFAULT FALSE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP
      ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_post_member
        FOREIGN KEY (member_id)
            REFERENCES member(id)
            ON DELETE CASCADE
);

-- =========================================================
-- 3. post_image (피드 이미지)
-- =========================================================
CREATE TABLE post_image (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    post_id BIGINT NOT NULL,
    image_url VARCHAR(255) NOT NULL,
    image_order INT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_post_image_post
        FOREIGN KEY (post_id)
            REFERENCES post(id)
            ON DELETE CASCADE
);


-- =========================================================
-- 4. post_like (피드 좋아요)
-- =========================================================
CREATE TABLE post_like (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT NOT NULL,
    post_id BIGINT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uk_member_post_like
       UNIQUE (member_id, post_id),

    CONSTRAINT fk_post_like_member
       FOREIGN KEY (member_id)
           REFERENCES member(id)
           ON DELETE CASCADE,

    CONSTRAINT fk_post_like_post
       FOREIGN KEY (post_id)
           REFERENCES post(id)
           ON DELETE CASCADE
);


-- =========================================================
-- 5. reply (피드 댓글)
-- =========================================================
CREATE TABLE reply (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    post_id BIGINT NOT NULL,
    member_id BIGINT NOT NULL,
    content TEXT NOT NULL,
    like_count INT NOT NULL DEFAULT 0,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP
       ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_reply_post
       FOREIGN KEY (post_id)
           REFERENCES post(id)
           ON DELETE CASCADE,

    CONSTRAINT fk_reply_member
       FOREIGN KEY (member_id)
           REFERENCES member(id)
           ON DELETE CASCADE
);


-- =========================================================
-- 6. reply_like (피드 댓글 좋아요)
-- =========================================================
CREATE TABLE reply_like (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    reply_id BIGINT NOT NULL,
    member_id BIGINT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uk_member_reply_like
        UNIQUE (member_id, reply_id),

    CONSTRAINT fk_reply_like_reply
        FOREIGN KEY (reply_id)
            REFERENCES reply(id)
            ON DELETE CASCADE,

    CONSTRAINT fk_reply_like_member
        FOREIGN KEY (member_id)
            REFERENCES member(id)
            ON DELETE CASCADE
);


-- =========================================================
-- 7. block (차단)
-- =========================================================
CREATE TABLE block (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT NOT NULL,
    blocked_id BIGINT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uk_member_blocked
       UNIQUE (member_id, blocked_id),

    CONSTRAINT fk_block_member
       FOREIGN KEY (member_id)
           REFERENCES member(id)
           ON DELETE CASCADE,

    CONSTRAINT fk_block_blocked_member
       FOREIGN KEY (blocked_id)
           REFERENCES member(id)
           ON DELETE CASCADE
);


-- =========================================================
-- 8. report (신고)
-- =========================================================
CREATE TABLE report (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT NOT NULL,
    target_id BIGINT NOT NULL,
    post_id BIGINT NULL,
    reply_id BIGINT NULL,
    target_type VARCHAR(20) NOT NULL,
    reason TEXT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'WAIT',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NULL,

    CONSTRAINT fk_report_member
        FOREIGN KEY (member_id)
            REFERENCES member(id)
            ON DELETE CASCADE,

    CONSTRAINT fk_report_target_member
        FOREIGN KEY (target_id)
            REFERENCES member(id)
            ON DELETE CASCADE,

    CONSTRAINT fk_report_post
        FOREIGN KEY (post_id)
            REFERENCES post(id)
            ON DELETE CASCADE,

    CONSTRAINT fk_report_reply
        FOREIGN KEY (reply_id)
            REFERENCES reply(id)
            ON DELETE CASCADE
);


-- =========================================================
-- 9. notification (알림)
-- =========================================================
CREATE TABLE notification (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    receiver_id BIGINT NOT NULL,
    sender_id BIGINT NOT NULL,
    type VARCHAR(100) NOT NULL,
    content VARCHAR(255) NOT NULL,
    is_read BOOLEAN NOT NULL DEFAULT FALSE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_notification_receiver
        FOREIGN KEY (receiver_id)
            REFERENCES member(id)
            ON DELETE CASCADE,

    CONSTRAINT fk_notification_sender
        FOREIGN KEY (sender_id)
            REFERENCES member(id)
            ON DELETE CASCADE
);


-- =========================================================
-- 10. follow (팔로우 / 팔로잉)
-- =========================================================
CREATE TABLE follow (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT NOT NULL,
    target_id BIGINT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uk_member_follow
        UNIQUE (member_id, target_id),

    CONSTRAINT fk_follow_member
        FOREIGN KEY (member_id)
            REFERENCES member(id)
            ON DELETE CASCADE,

    CONSTRAINT fk_follow_target_member
        FOREIGN KEY (target_id)
            REFERENCES member(id)
            ON DELETE CASCADE
);


-- =========================================================
-- 11. subscription (구독)
-- =========================================================
CREATE TABLE subscription (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    creator_id BIGINT NOT NULL,
    member_id BIGINT NOT NULL,
    customer_uid VARCHAR(100) NULL,
    price INT NOT NULL,
    start_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    end_date DATETIME NULL,
    status VARCHAR(20) NOT NULL,
    next_billing_at DATETIME NOT NULL,

    CONSTRAINT fk_subscription_creator
        FOREIGN KEY (creator_id)
            REFERENCES member(id)
            ON DELETE CASCADE,

    CONSTRAINT fk_subscription_member
        FOREIGN KEY (member_id)
            REFERENCES member(id)
            ON DELETE CASCADE,

    CONSTRAINT uk_subscription_creator_member
        UNIQUE (creator_id, member_id)
);


-- =========================================================
-- 12. payment (결제 이력)
-- =========================================================
CREATE TABLE payment (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT NOT NULL,
    subscription_id BIGINT NOT NULL,
    imp_uid VARCHAR(100) NULL,
    merchant_uid VARCHAR(100) NOT NULL UNIQUE,
    amount INT NOT NULL,
    status VARCHAR(20) NOT NULL,
    pay_method VARCHAR(30) NOT NULL,
    paid_at DATETIME NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_payment_member
        FOREIGN KEY (member_id)
            REFERENCES member(id)
            ON DELETE CASCADE,

    CONSTRAINT fk_payment_subscription
        FOREIGN KEY (subscription_id)
            REFERENCES subscription(id)
            ON DELETE CASCADE
);