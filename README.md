# Ecommerce Project

## Описание
Это учебный проект на **Spring Boot** для управления заказами в интернет-магазине.

Проект использует:
- Spring Boot 3.5.5
- Spring Data JPA для работы с базой данных
- H2 Database в качестве встроенной базы данных
- Spring Security + JWT для авторизации
- Swagger / SpringDoc для документирования
- Docker

---

## Установка и запуск

### H2 Консоль
Доступна по адресу: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)  

**Параметры подключения:**
- JDBC URL: `jdbc:h2:file:C:/Users/ACER/test`
- User: `sa`
- Password: (оставить пустым)

### API Документация
Swagger UI доступен по адресу: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)  

Здесь можно просмотреть и протестировать все доступные REST API.

---

## Авторизация
Проект использует JWT-токены.  

Для получения токена:
http
POST /auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "password"
}
Используйте полученный токен в заголовке:
Authorization: Bearer <token>
для доступа к защищённым эндпоинтам.


Тесты
Для запуска тестов используйте:
./gradlew test
Отчёт о покрытии тестами находится в:
build/reports/tests/test/index.html

Docker
Сборка Docker-образа
docker build -t ecommerce-app .
Запуск контейнера
docker run -p 8080:8080 --name ecommerce-app ecommerce-app
Структура проекта
src/main/java/com/example/ecommerce
├─ controller/   # REST контроллеры
├─ entity/       # JPA сущности
├─ repository/   # Spring Data JPA репозитории
├─ security/     # Конфигурация Spring Security, JWT
├─ service/      # Сервисы

Клонирование репозитория
git clone https://github.com/Umarkhn1/Ecommerce-project.git
cd Ecommerce-project
