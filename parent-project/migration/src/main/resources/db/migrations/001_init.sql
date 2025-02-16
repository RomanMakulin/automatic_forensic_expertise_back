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

-- Базовая сущность тарифного плана
CREATE TABLE Plan (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(), -- Уникальный идентификатор тарифа
    name VARCHAR(100) NOT NULL, -- Название тарифа (например, "Базовый", "Продвинутый", "Профессиональный")
    description TEXT NOT NULL, -- Описание тарифного плана (полный список доступных возможностей)
    price DECIMAL(10, 2) NOT NULL, -- Стоимость тарифа в месяц
    storage_limit INT NOT NULL, -- Лимит объема хранилища (в ГБ)

    max_users INT NOT NULL DEFAULT 1, -- Максимальное количество пользователей (экспертов) в тарифе
    max_documents INT, -- Количество бесплатных выгрузок заключений
    additional_document_price DECIMAL(10,2), -- Цена за каждую дополнительную выгрузку заключения после превышения лимита
    has_document_constructor BOOLEAN NOT NULL DEFAULT FALSE, -- Есть ли конструктор судебных заключений?
    has_legal_database_access BOOLEAN NOT NULL DEFAULT FALSE, -- Доступ к базовой нормативно-правовой базе?
    has_advanced_legal_database BOOLEAN NOT NULL DEFAULT FALSE, -- Доступ к расширенной нормативно-правовой базе?
    has_templates BOOLEAN NOT NULL DEFAULT FALSE, -- Доступны ли шаблоны для экспертов?
    templates_count INT DEFAULT 0, -- Количество доступных шаблонов
    has_expert_support BOOLEAN NOT NULL DEFAULT FALSE, -- Есть ли поддержка по судебным экспертизам?
    has_review_functionality BOOLEAN NOT NULL DEFAULT FALSE, -- Доступна ли функция проверки и рецензирования заключений?
    max_reviews INT, -- Количество рецензий (если рецензирование доступно)
    unlimited_documents BOOLEAN NOT NULL DEFAULT FALSE, -- Можно ли выгружать неограниченное количество заключений?
    active BOOLEAN NOT NULL DEFAULT TRUE, -- Активен ли тарифный план?
    created_at TIMESTAMP DEFAULT NOW(), -- Дата создания тарифа
    updated_at TIMESTAMP DEFAULT NOW() -- Дата последнего обновления тарифа
);

CREATE TABLE Plan_Features ( -- чтобы хранить дополнительные возможности, которые не подходят под фиксированные поля в Plan
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(), -- Уникальный идентификатор записи о функциональной возможности
    plan_id UUID NOT NULL, -- Ссылка на тарифный план, к которому относится возможность
    feature TEXT NOT NULL, -- Название или описание функциональной возможности
    FOREIGN KEY (plan_id) REFERENCES Plan (id) ON DELETE CASCADE -- Если тариф удаляется, связанные возможности тоже удаляются
);



CREATE TABLE Profile
(
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id             UUID UNIQUE NOT NULL,
    photo               VARCHAR(255),
    template            VARCHAR(255),
    passport            VARCHAR(255),
    diplom              VARCHAR(255),
    phone               VARCHAR(15) NOT NULL,
    location_id         UUID        NOT NULL,
    status_id           UUID        NOT NULL,
    plan_id             UUID,
    plan_start_date     DATE,
    plan_duration_month INT,
    CONSTRAINT fk_profile_user FOREIGN KEY (user_id) REFERENCES App_User (id) ON DELETE CASCADE,
    CONSTRAINT fk_profile_location FOREIGN KEY (location_id) REFERENCES Location (id),
    CONSTRAINT fk_profile_status FOREIGN KEY (status_id) REFERENCES Status (id),
    CONSTRAINT fk_profile_plan FOREIGN KEY (plan_id) REFERENCES Plan (id)
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
    path        VARCHAR(255),
    upload_date TIMESTAMP        DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT fk_profile_file FOREIGN KEY (profile_id) REFERENCES Profile (id) ON DELETE CASCADE
);

CREATE TABLE password_reset_token
(
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    token       VARCHAR(255) NOT NULL UNIQUE,
    user_id     UUID         NOT NULL,
    expiry_date TIMESTAMP    NOT NULL,
    FOREIGN KEY (user_id) REFERENCES app_user (id) ON DELETE CASCADE
);

