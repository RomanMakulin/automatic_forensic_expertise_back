-- 1. Таблица Role
CREATE TABLE Role
(
    id   UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(50) NOT NULL
);

-- 2. Таблица AppUser
CREATE TABLE App_User
(
    id                UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    full_name         VARCHAR(100)                               NOT NULL,
    email             VARCHAR(100) UNIQUE                        NOT NULL,
    registration_date TIMESTAMP        DEFAULT CURRENT_TIMESTAMP NOT NULL,
    role_id           UUID                                       NOT NULL,
    keycloak_id       VARCHAR(255)                               NOT NULL,
    CONSTRAINT fk_app_user_role FOREIGN KEY (role_id) REFERENCES Role (id)
);

-- 3. Таблица Status
CREATE TABLE Status
(
    id   UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(50) NOT NULL,
    verification_result VARCHAR(50) NOT NULL,
    activity_status     VARCHAR(50) NOT NULL,
    description         TEXT        NOT NULL
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

-- 6. Таблица Profile
CREATE TABLE Profile
(
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id     UUID UNIQUE  NOT NULL,
    photo       VARCHAR(255) NOT NULL,
    phone       VARCHAR(15)  NOT NULL,
    location_id UUID         NOT NULL,
    status_id   UUID         NOT NULL,
    CONSTRAINT fk_profile_user      FOREIGN KEY (user_id)       REFERENCES App_User (id),
    CONSTRAINT fk_profile_location  FOREIGN KEY (location_id)   REFERENCES Location (id),
    CONSTRAINT fk_profile_status    FOREIGN KEY (status_id)     REFERENCES Status (id)
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
    CONSTRAINT fk_profile_direction_profile     FOREIGN KEY (profile_id) REFERENCES Profile (id),
    CONSTRAINT fk_profile_direction_direction   FOREIGN KEY (direction_id) REFERENCES Direction (id)
);

-- 9. Таблица File
CREATE TABLE File
(
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    profile_id  UUID                                       NOT NULL,
    description VARCHAR(255)                               NOT NULL,
    type        VARCHAR(50)                                NOT NULL,
    path        VARCHAR(255)                               NOT NULL,
    upload_date TIMESTAMP        DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT fk_profile_file FOREIGN KEY (profile_id) REFERENCES Profile (id)
);

