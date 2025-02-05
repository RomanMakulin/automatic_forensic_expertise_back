# Инструкция по запуску многомодульного проекта "Судебные экспертизы"

## Шаги запуска системы

1. **Запуск Keycloak**  
   В корневой папке проекта (parent-project) выполните команду:
   ```bash
   docker-compose up
   ```


2. **Запуск Spring Cloud Config Service**  
   Запустите сервис `config-server` для загрузки общих настроек приложений.


3. **Запуск сервиса авторизации**  
   Запустите сервис `auth`, который предоставляет функционал логина (получение токена авторизации запросов), регистрации
   и восстановления пароля.

   ```
   Обратите внимание, вам необходимо внести в конфиг auth.yml СВОИ данные из вашего Keycloak (включая secret). 
   
   Для этого необходимо зайти в интерфейс кейклока по адресу `http://localhost:8081/`, 
   создать свой realm, перейти в него и создать client, указав при этом необходимые галочки: `Client authentication`, `Authorization`.
   
   Далее необходимо зайти в свой клиент, нажать на кнопку `Action` (справа сверху) и `Download adapter config`. 
   Здесь вы найдете необходимые данные для конфигурации auth.yml (realm, resource, secret).
   ```

4. **Запуск сервиса уведомлений**  
   Одновременно с сервисом авторизации обязательно запускайте `mail-notification`, так как он отвечает за отправку
   почтовых уведомлений (регистрация, восстановление пароля).

---

5. **Опциональный запуск других сервисов**
    - Остальные сервисы запускайте по необходимости в зависимости от тестируемого функционала.

    - Если вам нужно работать с файлами - фото пользователя, шаблон пользователя, файлы пользователя (сканы
      сертификатов, паспорта и т.д..), обязательно запустите `minio-service` через `docker-compose up` в корне сервиса.

---

## Базовые сервисы

| Сервис       | URL                     | Описание               |
|--------------|-------------------------|------------------------|
| Notification | `http://localhost:8087` | Сервис уведомлений     |
| Auth         | `http://localhost:8095` | Сервис аутентификации  |
| Frontend     | `http://localhost:8010` | Фронтенд               |
| Profile      | `http://localhost:8090` | Сервис профилей        |
| Admin        | `http://localhost:8020` | Админ-сервис           |
| MinIO        | `http://localhost:8030` | Хранилище файлов       |
| Cloud        | `http://localhost:8888` | Облачный конфиг-сервис |
| Tariff       | `http://localhost:8085` | Сервис тарифов         |

## Сервисы в docker

| Сервис       | URL                     | Описание             |
|--------------|-------------------------|----------------------|
| Keycloak WEB | `http://localhost:8081` | Безопасность проекта |
| MinIO WEB    | `http://localhost:9001` | Интерфейс minIO      |
| MinIO S3 API | `http://localhost:8010` | API к minIO          |

---

## Основные API-эндпоинты

### Notification

- Отправка почты: `http://localhost:8087/api/notification/send`
- Публичная отправка почты: `http://localhost:8087/api/public-notification/send`

### Auth

- Сброс пароля: `http://localhost:8095/api/auth/reset-password`
- Подтверждение email: `http://localhost:8095/api/auth/verify-email`

### Administration

- Получение всех профилей: `http://localhost:8020/api/profile`
- Непроверенные профили: `http://localhost:8020/api/profile/unverified`
- Верификация профиля: `http://localhost:8020/api/profile/verify`
- Отмена валидации профиля: `http://localhost:8020/api/profile/cancel-validation`

### MinIO

- Загрузка файла: `http://localhost:8030/api/file/upload`


---

## Примеры запросов

### Регистрация
**URL:** `http://localhost:8095/api/auth/register`  
**Метод:** POST  
**Body:**
```json
{
    "email": "sup.makulin@mail.ru",
    "password": "123456",
    "first_name": "Roman",
    "last_name": "Makulin"
}
```

### Логин
**URL:** `http://localhost:8095/api/auth/login`  
**Метод:** POST  
**Body:**
```json
{
  "email": "sup.makulin@mail.ru",
  "password": "123456"
}
```

### Сброс пароля
**URL:** `http://localhost:8080/api/auth/reset-password/sup.makulin@mail.ru`  - в url PathVariable email

**Метод:** GET

