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

- Загрузка файла: `http://localhost:8030/api/files/upload-file`
- Загрузка нескольких файлов: `http://localhost:8030/api/files/upload-files`
- Загрузка всех файлов (аватар, шаблон, файлы): `http://localhost:8030/api/files/upload-all`
- Удаление фото: `http://localhost:8030/api/files/delete-photo`
- Удаление шаблона: `http://localhost:8030/api/files/delete-template`
- Удаление файла: `http://localhost:8030/api/files/delete-file`
- Удаление нескольких файлов: `http://localhost:8030/api/files/delete-file-list`
- Получение фото пользователя: `http://localhost:8030/api/files/get-photo`
- Получение шаблона пользователя: `http://localhost:8030/api/files/get-template`
- Получение списка файлов пользователя: `http://localhost:8030/api/files/get-files`

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

**Curl:**
```
curl --location 'http://localhost:8095/api/auth/register' \
--data-raw '{
    "email": "sup.makulin@mail.ru",
    "password": "123456",
    "first_name": "Roman",
    "last_name": "Makulin"
}
'
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

**Curl:**
```
curl --location 'http://localhost:8095/api/auth/login' \
--header 'Content-Type: application/json' \
--data-raw '{
  "email": "sup.makulin@mail.ru",
  "password": "123456"
}'
```

### Сброс пароля
**URL:** `http://localhost:8080/api/auth/reset-password/sup.makulin@mail.ru`  - в url PathVariable email

**Метод:** GET

**Curl:**
```
curl --location 'http://localhost:8080/api/auth/reset-password/sup.makulin@mail.ru'
```


## Модуль Профиля


### Создание профиля
**URL:** `http://localhost:8090/api/profile/create`  
**Метод:** POST  
**Body:**
```form-data
profile: json
photo: MultipartFile
files: MultipartFile
files: MultipartFile
```

```json
{
   "phone": "+1234567890",
   "locationDTO": {
      "country": "Россия",
      "region": "Московская область",
      "city": "Москва",
      "address": "Красная площадь, 1"
   },
   "directionDTOList": [
      {
         "name": "IT"
      },
      {
         "name": "Финансы"
      }
   ]
}

```

**Curl:**
```
curl --location 'http://localhost:8090/api/profile/create' \
--header 'Authorization: Bearer JWTToken' \
--header 'Cookie: JSESSIONID=18844149A5E9CCE31AEA024E3939295E' \
--form 'profile="{
  \"phone\": \"+1234567890\",
  \"locationDTO\": {
    \"country\": \"Россия\",
    \"region\": \"Московская область\",
    \"city\": \"Москва\",
    \"address\": \"Красная площадь, 1\"
  },
  \"directionDTOList\": [
    {
      \"name\": \"IT\"
    },
    {
      \"name\": \"Финансы\"
    }
  ]
}

";type=application/json' \
--form 'photo=@"/C:/Users/ADIKIA/Desktop/111.jpg"' \
--form 'files=@"/C:/Users/ADIKIA/Desktop/доки 1.pdf"' \
--form 'files=@"/C:/Users/ADIKIA/Desktop/доки 2.pdf"'
```


### Получение профиля
**URL:** `http://localhost:8090/api/profile` 

**Метод:** GET

**Curl:**
```
curl --location 'http://localhost:8090/api/profile/?id=782d26a5-775d-4f80-9135-1d6cf1cce3a9' \
--header 'Authorization: Bearer JWTToken' \
--header 'Cookie: JSESSIONID=18844149A5E9CCE31AEA024E3939295E'
```


### Работа с файлами через minIO
[Кликабельно](MINIO.md)
