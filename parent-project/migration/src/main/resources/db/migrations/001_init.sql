CREATE TABLE Role
(
    id   UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(50) NOT NULL
);

CREATE TABLE App_User
(
    id                 UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    full_name          VARCHAR(100)                               NOT NULL,
    email              VARCHAR(100) UNIQUE                        NOT NULL,
    registration_date  TIMESTAMP        DEFAULT CURRENT_TIMESTAMP NOT NULL,
    role_id            UUID                                       NOT NULL,
    keycloak_id        VARCHAR(255)                               NOT NULL,
    verification_email BOOLEAN          DEFAULT FALSE             NOT NULL,
    CONSTRAINT fk_app_user_role FOREIGN KEY (role_id) REFERENCES Role (id)
);

CREATE TABLE Status
(
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name                VARCHAR(50) NOT NULL,
    verification_result VARCHAR(50) NOT NULL,
    activity_status     VARCHAR(50) NOT NULL
);

CREATE TABLE Location
(
    id      UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    country VARCHAR(100) NOT NULL,
    region  VARCHAR(100) NOT NULL,
    city    VARCHAR(100) NOT NULL,
    address VARCHAR(255) NOT NULL
);

CREATE TABLE Profile
(
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id     UUID UNIQUE  NOT NULL,
    photo       VARCHAR(255) NOT NULL,
    phone       VARCHAR(15)  NOT NULL,
    location_id UUID         NOT NULL,
    status_id   UUID         NOT NULL,
    CONSTRAINT fk_profile_user FOREIGN KEY (user_id) REFERENCES App_User (id) ON DELETE CASCADE,
    CONSTRAINT fk_profile_location FOREIGN KEY (location_id) REFERENCES Location (id),
    CONSTRAINT fk_profile_status FOREIGN KEY (status_id) REFERENCES Status (id)
);

CREATE TABLE Direction
(
    id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    profile_id UUID         NOT NULL,
    name       VARCHAR(100) NOT NULL,
    CONSTRAINT fk_profile_direction_profile FOREIGN KEY (profile_id) REFERENCES Profile (id) ON DELETE CASCADE
);

CREATE TABLE File
(
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    profile_id  UUID                                       NOT NULL,
    description VARCHAR(255)                               NOT NULL,
    type        VARCHAR(50)                                NOT NULL,
    path        VARCHAR(255)                               NOT NULL,
    upload_date TIMESTAMP        DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT fk_profile_file FOREIGN KEY (profile_id) REFERENCES Profile (id) ON DELETE CASCADE
);

CREATE TABLE Plan
(
    id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    profile_id    UUID           NOT NULL,
    name          VARCHAR(100)   NOT NULL,
    description   TEXT           NOT NULL,
    price         DECIMAL(10, 2) NOT NULL,
    storage_limit INT            NOT NULL,
    start_date    DATE           NOT NULL,
    end_date      DATE           NOT NULL,
    CONSTRAINT fk_profile_plan_profile FOREIGN KEY (profile_id) REFERENCES Profile (id) ON DELETE CASCADE
);

CREATE TABLE Template
(
    id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    profile_id UUID                                       NOT NULL,
    name       VARCHAR(255)                               NOT NULL,
    type       VARCHAR(50)                                NOT NULL,
    path       VARCHAR(255)                               NOT NULL,
    created_at TIMESTAMP        DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT fk_profile_template_profile FOREIGN KEY (profile_id) REFERENCES Profile (id) ON DELETE CASCADE
);

CREATE TABLE password_reset_token
(
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    token       VARCHAR(255) NOT NULL UNIQUE,
    user_id     UUID         NOT NULL,
    expiry_date TIMESTAMP    NOT NULL,
    FOREIGN KEY (user_id) REFERENCES app_user (id) ON DELETE CASCADE
);
