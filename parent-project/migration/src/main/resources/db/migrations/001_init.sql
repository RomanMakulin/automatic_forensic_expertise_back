-- 1. Таблица Role
CREATE TABLE Role
(
    id   UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(50) NOT NULL
);

-- 2. Таблица AppUser
CREATE TABLE AppUser
(
    id                UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    full_name         VARCHAR(100)                               NOT NULL,
    email             VARCHAR(100) UNIQUE                        NOT NULL,
    registration_date TIMESTAMP        DEFAULT CURRENT_TIMESTAMP NOT NULL,
    role_id           UUID                                       NOT NULL,
    keycloak_id       VARCHAR(255)                               NOT NULL,
    CONSTRAINT fk_appuser_role FOREIGN KEY (role_id) REFERENCES Role (id)
);

-- 3. Таблица Status
CREATE TABLE Status
(
    id   UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(50) NOT NULL
);

-- 4. Таблица Location
CREATE TABLE Location
(
    id      UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    country VARCHAR(100) NOT NULL,
    region  VARCHAR(100) NOT NULL,
    city    VARCHAR(100) NOT NULL,
    address VARCHAR(255) NOT NULL
);

-- 5. Таблица Profile_Status
CREATE TABLE Profile_Status
(
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    verification_result VARCHAR(50) NOT NULL,
    activity_status     VARCHAR(50) NOT NULL,
    description         TEXT        NOT NULL
);

-- 6. Таблица Profile
CREATE TABLE Profile
(
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id     UUID UNIQUE  NOT NULL,
    photo       VARCHAR(255) NOT NULL,
    phone       VARCHAR(15)  NOT NULL,
    location_id UUID         NOT NULL,
    status_id   UUID         NOT NULL,
    last_login  TIMESTAMP    NOT NULL,
    CONSTRAINT fk_profile_user FOREIGN KEY (user_id) REFERENCES AppUser (id),
    CONSTRAINT fk_profile_location FOREIGN KEY (location_id) REFERENCES Location (id),
    CONSTRAINT fk_profile_status FOREIGN KEY (status_id) REFERENCES Profile_Status (id)
);

-- 7. Таблица Direction
CREATE TABLE Direction
(
    id   UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100) NOT NULL
);

-- 8. Таблица Profile_Direction
CREATE TABLE Profile_Direction
(
    id           UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    profile_id   UUID NOT NULL,
    direction_id UUID NOT NULL,
    CONSTRAINT fk_profile_direction_profile FOREIGN KEY (profile_id) REFERENCES Profile (id),
    CONSTRAINT fk_profile_direction_direction FOREIGN KEY (direction_id) REFERENCES Direction (id)
);

-- 9. Таблица File
CREATE TABLE File
(
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    description VARCHAR(255)                               NOT NULL,
    type        VARCHAR(50)                                NOT NULL,
    path        VARCHAR(255)                               NOT NULL,
    upload_date TIMESTAMP        DEFAULT CURRENT_TIMESTAMP NOT NULL
);

-- 10. Таблица Profile_File
CREATE TABLE Profile_File
(
    id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    profile_id UUID NOT NULL,
    file_id    UUID NOT NULL,
    CONSTRAINT fk_profile_file_profile FOREIGN KEY (profile_id) REFERENCES Profile (id),
    CONSTRAINT fk_profile_file_file FOREIGN KEY (file_id) REFERENCES File (id)
);

-- 11. Таблица Plan
CREATE TABLE Plan
(
    id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name          VARCHAR(100)   NOT NULL,
    description   TEXT           NOT NULL,
    price         NUMERIC(10, 2) NOT NULL,
    storage_limit INT            NOT NULL,
    start_date    DATE           NOT NULL,
    end_date      DATE           NOT NULL
);

-- 12. Таблица Profile_Plan
CREATE TABLE Profile_Plan
(
    id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    plan_id    UUID NOT NULL,
    profile_id UUID NOT NULL,
    CONSTRAINT fk_profile_plan_plan FOREIGN KEY (plan_id) REFERENCES Plan (id),
    CONSTRAINT fk_profile_plan_profile FOREIGN KEY (profile_id) REFERENCES Profile (id)
);

-- 13. Таблица Template (аналог File)
CREATE TABLE Template
(
    id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name       VARCHAR(255)                               NOT NULL,
    type       VARCHAR(50)                                NOT NULL,
    path       VARCHAR(255)                               NOT NULL,
    created_at TIMESTAMP        DEFAULT CURRENT_TIMESTAMP NOT NULL
);

-- 14. Таблица Profile_Template (аналог Profile_File)
CREATE TABLE Profile_Template
(
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    profile_id  UUID NOT NULL,
    template_id UUID NOT NULL,
    CONSTRAINT fk_profile_template_profile FOREIGN KEY (profile_id) REFERENCES Profile (id),
    CONSTRAINT fk_profile_template_template FOREIGN KEY (template_id) REFERENCES Template (id)
);
