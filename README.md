# Тестовое задание — автотесты для API аутентификации

Автоматизированные тесты для REST-сервиса на Spring Boot, покрывающие единственный эндпоинт `POST /endpoint` (действия `LOGIN` / `ACTION` / `LOGOUT`).

## Стек

- Java 17
- JUnit 5
- WireMock — эмуляция внешнего сервиса (`/auth`, `/doAction`)
- Allure — отчётность
- Maven

## Структура проекта

```
src/test/java/dev/zhulidov/tests/
├── support/
│   └── BaseTest.java          — общая инфраструктура: поднятие WireMock, генерация токена, отправка запроса
├── LoginTest.java              — сценарии LOGIN
├── ActionTest.java             — сценарии ACTION
├── LogoutTest.java             — сценарии LOGOUT
├── ConcurrencyTest.java        — сценарии конкурентного доступа 
└── RequestValidationTest.java  — валидация запроса (API key, формат токена, обязательные параметры)
```

В репозитории — только код тестов. Тестируемое приложение (`internal-0.0.1-SNAPSHOT.jar`) не публикуется вместе с тестами и запускается отдельно.

## Как запустить

### 1. Запустить тестируемое приложение

```bash
java -jar -Dsecret=qazWSXedc -Dmock=http://localhost:8888/ internal-0.0.1-SNAPSHOT.jar
```

Приложение должно быть доступно на `http://localhost:8080` до запуска тестов. Порт `8888` должен быть свободным — туда тесты сами поднимут мок внешнего сервиса на время прогона.

### 2. Прогнать тесты и открыть отчёт

```bash
mvn clean test allure:serve
```

Откроется браузер с интерактивным Allure-отчётом.

Если нужен только статический HTML-отчёт без запуска локального сервера:

```bash
mvn clean test allure:report
```

Отчёт будет доступен в `target/site/allure-maven-plugin/index.html`.

## Тест-план 

Подробное описание покрытых сценариев, тест-стратегии  — в [REPORT.md](REPORT.md).
