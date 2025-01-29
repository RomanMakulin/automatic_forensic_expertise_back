CREATE TABLE password_reset_token
(
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    token       VARCHAR(255) NOT NULL UNIQUE,
    user_id     UUID         NOT NULL,
    expiry_date TIMESTAMP    NOT NULL,
    FOREIGN KEY (user_id) REFERENCES app_user (id) ON DELETE CASCADE
);