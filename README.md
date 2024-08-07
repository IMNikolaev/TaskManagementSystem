Task Management System
Этот проект представляет собой систему управления задачами с поддержкой аутентификации и авторизации пользователей, создания и управления задачами, а также комментариями к ним.

Требования
1) Java 17

2) Maven 3.8.1 или выше
3) Docker и Docker Compose (для запуска базы данных)
4) Flyway (для миграций базы данных)

https://github.com/IMNikolaev/TaskManagementSystem

1) Необходимо включить приложение DOCKER
2) Перейдите в каталог проекта TaskManagementSystem
3) TaskManagementSystem -> src -> main -> java -> docker -> docker-compose.yaml
4) Скомпилировать файл docker-compose.yaml (docker-compose up -d)
5) Для запуска приложения необходимо перейти в
6) TaskManagementSystem -> src -> main -> java -> TMS 
7) Запустить файл Application (Run 'Application.main()')

Swagger UI: http://localhost:8192/tms/swagger-ui/index.html

OpenAPI Specification: http://localhost:8192/tms/v3/api-docs

Структура проекта
src/main/java: Исходный код приложения

TMS/config: Конфигурация приложения (например, безопасность, JWT)

TMS/controllers: Контроллеры для обработки запросов

TMS/dto: Объекты передачи данных (DTO)

TMS/entities: Сущности, используемые в приложении

TMS/services: Сервисы для бизнес-логики

src/main/resources: Ресурсы, такие как конфигурационные файлы

src/test/java: Тесты для приложения