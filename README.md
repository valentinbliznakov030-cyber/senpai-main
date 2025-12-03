# Senpai Main - Backend Service

Spring Boot приложение за аниме платформа с поддръжка за видео стрийминг, субтитри и превод.

## 📋 Съдържание

- [Изисквания](#изисквания)
- [Инсталация](#инсталация)
- [Външни API-та и сервиси](#-външни-api-та-и-сервиси)
- [Docker Setup](#docker-setup)
- [Стартиране на приложението](#стартиране-на-приложението)
- [Конфигурация](#-конфигурация)
- [Redis кеширане](#redis-кеширане)

## 🔧 Изисквания

### Основни зависимости

- **Java 17** или по-нова версия
- **Maven 3.6+**
- **MySQL 8.0+**
- **Redis** (за кеширане)
- **Docker** (за LibreTranslate)

### Външни инструменти

Приложението комуникира с `anime-service`, който изисква следните инструменти:

#### 1. nm3u8dl_re.exe

**Какво е:**
- Инструмент за изтегляне на M3U8 видео стриймове
- Използва се от `anime-service` за извличане на M3U8 линкове
- Standalone executable (C# версия) - **НЕ изисква Python**
- Използва `ffmpeg` за обработка на видео

**Важно:**
- `nm3u8dl_re.exe` е standalone - работи директно, без допълнителни зависимости
- Изисква само `ffmpeg` (който също е standalone)
- **НЕ изисква Python**

**Инсталация:**
1. Изтегли от: https://github.com/nilaoda/N_m3u8DL-CLI/releases
2. Постави `nm3u8dl_re.exe` в папка, която е в PATH или в папката на `anime-service`
3. Увери се че имаш права за изпълнение
4. Увери се че `ffmpeg` е инсталиран (виж по-долу)

**Проверка:**
```bash
nm3u8dl_re.exe --version
```

#### 2. FFmpeg

**Какво е:**
- Инструмент за обработка на видео и аудио файлове
- Използва се от `anime-service` за конвертиране и обработка на видео
- Използва се от `nm3u8dl_re.exe` за обработка на M3U8 стриймове
- Standalone инструмент - **НЕ изисква Python**

**Инсталация:**

**Windows:**
1. Изтегли от: https://ffmpeg.org/download.html
2. Разархивирай в папка (напр. `C:\ffmpeg`)
3. Добави в PATH:
   - Отвори "Environment Variables"
   - Добави `C:\ffmpeg\bin` в PATH

**Linux:**
```bash
sudo apt update
sudo apt install ffmpeg
```

**macOS:**
```bash
brew install ffmpeg
```

**Проверка:**
```bash
ffmpeg -version
```

#### 3. Python (опционално)

**Кога е нужен:**
- **САМО** ако искаш да инсталираш LibreTranslate локално (не в Docker)
- `nm3u8dl_re.exe` и `ffmpeg` **НЕ изискват Python** - те са standalone инструменти

**Важно:**
- `nm3u8dl_re.exe` е C# версия (standalone executable) - работи без Python
- `ffmpeg` е standalone инструмент - работи без Python
- Python е нужен **САМО** за локална инсталация на LibreTranslate
- Ако използваш LibreTranslate в Docker (препоръчително), Python **НЕ е нужен**

**Инсталация (само ако искаш локална LibreTranslate):**
1. Изтегли от: https://www.python.org/downloads/
2. При инсталация избери "Add Python to PATH"
3. Проверка:
```bash
python --version
```

## 🐳 Docker Setup

### LibreTranslate

Приложението използва LibreTranslate за превод на субтитри. Има два начина за инсталация:

#### Вариант 1: С Docker (Препоръчително)

**Предимства:** Не изисква Python, по-лесно за управление

**Стартиране:**
```bash
docker run -d -p 5000:5000 libretranslate/libretranslate
```

Или с повече опции:
```bash
docker run -d \
  -p 5000:5000 \
  --name libretranslate \
  libretranslate/libretranslate
```

#### Вариант 2: Локална инсталация (Изисква Python)

**Изисквания:**
- Python 3.8+
- pip

**Инсталация:**
```bash
pip install libretranslate
libretranslate --host 0.0.0.0 --port 5000
```

**Забележка:** За локална инсталация трябва да имаш Python инсталиран.

#### Проверка на LibreTranslate

Отвори в браузър: http://localhost:5000

Или тествай с curl:
```bash
curl http://localhost:5000/languages
```

#### Конфигурация в application.properties

Увери се че `anime-service` е конфигуриран да използва LibreTranslate на правилния порт.

### Redis

За кеширане на данни се използва Redis:

```bash
docker run -d \
  --name redis-senpai \
  -p 6379:6379 \
  redis:latest
```

## 🚀 Стартиране на приложението

### Локално стартиране

1. **Стартирай MySQL:**
   - Увери се че MySQL работи на `localhost:3306`
   - Базата данни се създава автоматично при първо стартиране

2. **Стартирай Redis:**
   ```bash
   docker start redis-senpai
   ```

3. **Стартирай LibreTranslate (ако е нужно):**
   ```bash
   docker start libretranslate
   ```

4. **Стартирай приложението:**
   ```bash
   ./mvnw spring-boot:run
   ```
   
   Или с Maven:
   ```bash
   mvn spring-boot:run
   ```

### С Docker

1. **Build на Docker image:**
   ```bash
   docker build -t senpai-main .
   ```

2. **Стартирай контейнера:**
   ```bash
   docker run -d \
     -p 8080:8080 \
     --name senpai-main \
     --link redis-senpai:redis \
     senpai-main
   ```

## ⚙️ Конфигурация

### application.properties

Пълна конфигурация:

```properties
# Application
spring.application.name=senpai-main

# Database
spring.datasource.url=jdbc:mysql://localhost:3306/mydatabase?createDatabaseIfNotExists=true&allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=your_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA/Hibernate
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
spring.jpa.hibernate.ddl-auto=update

# Redis
spring.data.redis.host=localhost
spring.data.redis.port=6379
spring.data.redis.timeout=2000ms
spring.session.store-type=none

# External Services
anime.service.url=http://localhost:8081/api/v1/
subscription.service.url=http://localhost:8082/api/v1/subscriptions

# Email (Gmail SMTP)
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.starttls.required=true
spring.mail.properties.mail.smtp.ssl.trust=smtp.gmail.com

# File Upload
spring.web.resources.static-locations=file:uploads/
spring.servlet.multipart.enabled=true
spring.servlet.multipart.max-file-size=20MB
spring.servlet.multipart.max-request-size=20MB

# Feign Client Timeouts
feign.client.config.default.connectTimeout=600000
feign.client.config.default.readTimeout=600000

# Logging
logging.level.root=INFO
logging.level.org.springframework.web=DEBUG
logging.level.org.hibernate.SQL=DEBUG
```

### application-docker.properties

За Docker среда, използвай `application-docker.properties`:

```properties
# Database (Docker service name)
spring.datasource.url=jdbc:mysql://mysql:3306/mydatabase?createDatabaseIfNotExists=true&allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC

# Redis (Docker service name)
spring.data.redis.host=redis

# External Services (Docker service names)
anime.service.url=http://senpai-anime:8081/api/v1/
subscription.service.url=http://senpai-subscription:8082/api/v1/subscriptions
```

## 🔌 Външни API-та и сервиси

Приложението използва следните външни API-та и сервиси:

### 1. Anime Service (Feign Client)

**URL:** `http://localhost:8081/api/v1/` (локално) или `http://senpai-anime:8081/api/v1/` (Docker)

**Порт:** 8081

**Какво прави:**
- Обработка на M3U8 видео стриймове
- Създаване и обработка на видео файлове
- Изтегляне на субтитри
- Превод на субтитри (чрез LibreTranslate)

**API Endpoints (чрез AnimeClient):**
- `GET /anime/m3u8Link` - Получаване на M3U8 линк за видео
- `GET /stream` - Стрийминг на видео файл
- `POST /anime/video` - Създаване на видео
- `POST /subtitles` - Изтегляне на субтитри
- `POST /subtitles/translation` - Превод на субтитри

**Конфигурация:**
```properties
anime.service.url=http://localhost:8081/api/v1/
```

**Изисквания:**
- Трябва да е стартиран преди `senpai-main`
- Изисква `nm3u8dl_re.exe` и `ffmpeg` (виж по-горе)
- Комуникира с LibreTranslate за превод на субтитри

---

### 2. Subscription Service (Feign Client)

**URL:** `http://localhost:8082/api/v1/subscriptions` (локално) или `http://senpai-subscription:8082/api/v1/subscriptions` (Docker)

**Порт:** 8082

**Какво прави:**
- Управление на абонаменти
- Проверка на статус на абонамент
- Увеличаване на броя гледания
- Надграждане на абонамент

**API Endpoints (чрез SubscriptionClient):**
- `GET /{userId}` - Получаване на статус на абонамент (кеширано в Redis)
- `POST /increase/{userId}` - Увеличаване на броя гледания
- `PUT /upgrade/{userId}` - Надграждане на абонамент

**Конфигурация:**
```properties
subscription.service.url=http://localhost:8082/api/v1/subscriptions
```

**Изисквания:**
- Трябва да е стартиран преди `senpai-main`
- Използва се за проверка на права на потребителите

---

### 3. Gmail SMTP (Email Service)

**Host:** `smtp.gmail.com`

**Порт:** 587

**Какво прави:**
- Изпращане на имейли за нулиране на парола
- Изпращане на кодове за потвърждение

**Конфигурация:**
```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.starttls.required=true
spring.mail.properties.mail.smtp.ssl.trust=smtp.gmail.com
```

**Изисквания:**
- Gmail акаунт с активиран "App Password"
- За да получиш App Password:
  1. Отиди на Google Account Settings
  2. Security → 2-Step Verification (трябва да е активирана)
  3. App Passwords → Generate нов парола за "Mail"
  4. Използвай генерирания парола в `application.properties`

**Използване:**
- Изпраща се автоматично при заявка за нулиране на парола
- Кодът е валиден 10 минути

---

### 4. LibreTranslate (Translation Service)

**URL:** `http://localhost:5000`

**Порт:** 5000

**Какво прави:**
- Превод на субтитри на различни езици
- Използва се от `anime-service` за превод на субтитри

**Конфигурация:**
- Конфигурира се в `anime-service`, не директно в `senpai-main`
- Трябва да е достъпен от `anime-service`

**Изисквания:**
- Виж секцията [Docker Setup - LibreTranslate](#libretranslate) по-горе

---

### 5. MySQL Database

**URL:** `jdbc:mysql://localhost:3306/mydatabase` (локално) или `jdbc:mysql://mysql:3306/mydatabase` (Docker)

**Порт:** 3306

**Какво прави:**
- Съхранение на данни за потребители, аниме, епизоди, коментари, и др.

**Конфигурация:**
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/mydatabase?createDatabaseIfNotExists=true&allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=your_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
```

**Изисквания:**
- MySQL 8.0+ инсталиран и стартиран
- Базата данни се създава автоматично при първо стартиране

---

### 6. Redis (Cache Service)

**Host:** `localhost` (локално) или `redis` (Docker)

**Порт:** 6379

**Какво прави:**
- Кеширане на данни за подобряване на производителността
- Кеширане на Subscription Status, Member Profile, Member DTO

**Конфигурация:**
```properties
spring.data.redis.host=localhost
spring.data.redis.port=6379
spring.data.redis.timeout=2000ms
spring.session.store-type=none
```

**Изисквания:**
- Redis инсталиран или стартиран в Docker
- Виж секцията [Docker Setup - Redis](#redis) по-горе

---

### Резюме на портовете

| Сервис | Порт | Протокол | Локално URL | Docker URL |
|--------|------|----------|-------------|------------|
| senpai-main | 8080 | HTTP | http://localhost:8080 | http://senpai-main:8080 |
| anime-service | 8081 | HTTP | http://localhost:8081 | http://senpai-anime:8081 |
| subscription-service | 8082 | HTTP | http://localhost:8082 | http://senpai-subscription:8082 |
| LibreTranslate | 5000 | HTTP | http://localhost:5000 | http://libretranslate:5000 |
| MySQL | 3306 | TCP | localhost:3306 | mysql:3306 |
| Redis | 6379 | TCP | localhost:6379 | redis:6379 |
| Gmail SMTP | 587 | SMTP | smtp.gmail.com:587 | - |

### Важни забележки

- **Всички сервиси трябва да са стартирани** преди да стартираш `senpai-main`
- **anime-service** и **subscription-service** са отделни Spring Boot приложения
- **LibreTranslate** се използва от `anime-service`, не директно от `senpai-main`
- **Redis** е задължителен за кеширането да работи
- **MySQL** трябва да работи за приложението да функционира
- **Gmail SMTP** е опционален, но е нужен за функционалността за нулиране на парола

## 💾 Redis кеширане

Приложението използва Redis за кеширане на:

- **Subscription Status** - TTL: 1 час
- **Member Profile** - TTL: 30 минути
- **Member DTO** - TTL: 10 минути

Кеширането се конфигурира в `RedisConfig.java` и се използва чрез `@Cacheable` и `@CacheEvict` анотации.

## 📁 Структура на проекта

```
senpai-main/
├── src/main/java/bg/senpai_main/
│   ├── configs/          # Конфигурации (Redis, Security, etc.)
│   ├── web/              # REST контролери
│   ├── services/         # Бизнес логика
│   ├── entities/         # JPA ентитети
│   ├── dtos/             # Data Transfer Objects
│   └── repositories/      # JPA репозитории
├── src/main/resources/
│   └── application.properties
└── Dockerfile
```

## 🔍 Проверка на инсталацията

### Проверка на nm3u8dl_re.exe
```bash
nm3u8dl_re.exe --version
```

### Проверка на FFmpeg
```bash
ffmpeg -version
```

### Проверка на Redis
```bash
docker ps | grep redis
# или
redis-cli ping
# Трябва да върне: PONG
```

### Проверка на LibreTranslate
```bash
curl http://localhost:5000/languages
```

## 🐛 Troubleshooting

### Проблем: Redis не се свързва

**Решение:**
- Провери дали Redis контейнерът работи: `docker ps`
- Провери порта: `docker port redis-senpai`
- Провери настройките в `application.properties`

### Проблем: anime-service не отговаря

**Решение:**
- Увери се че `anime-service` е стартиран
- Провери URL-а в `application.properties`
- Провери дали `nm3u8dl_re.exe` и `ffmpeg` са инсталирани и в PATH

### Проблем: LibreTranslate не работи

**Решение:**
- Провери дали контейнерът работи: `docker ps | grep libretranslate`
- Провери логовете: `docker logs libretranslate`
- Провери порта: `curl http://localhost:5000/languages`

### Проблем: subscription-service не отговаря

**Решение:**
- Увери се че `subscription-service` е стартиран на порт 8082
- Провери URL-а в `application.properties`
- Провери дали сервисът е достъпен: `curl http://localhost:8082/api/v1/subscriptions/{userId}`

### Проблем: Gmail SMTP не работи

**Решение:**
- Увери се че използваш "App Password", не обикновената парола
- Провери дали 2-Step Verification е активирана в Google Account
- Провери дали порт 587 не е блокиран от firewall
- Тествай с: `telnet smtp.gmail.com 587`

### Проблем: Feign Client timeout

**Решение:**
- Увеличи timeout стойностите в `application.properties`:
  ```properties
  feign.client.config.default.connectTimeout=600000
  feign.client.config.default.readTimeout=600000
  ```
- Провери дали външните сервиси отговарят навреме

## 📝 Забележки

- `nm3u8dl_re.exe` и `ffmpeg` трябва да са достъпни от `anime-service`, не от `senpai-main`
- `nm3u8dl_re.exe` е standalone executable (C# версия) - **НЕ изисква Python**
- `nm3u8dl_re.exe` използва `ffmpeg` за обработка на видео, но и двата са standalone инструменти
- `ffmpeg` е standalone инструмент - **НЕ изисква Python**
- LibreTranslate трябва да работи преди да се използва функционалността за превод на субтитри
- **Python е нужен САМО** ако искаш локална инсталация на LibreTranslate (не в Docker)
- Ако използваш LibreTranslate в Docker, Python **НЕ е нужен**
- Redis трябва да работи за кеширането да функционира правилно

## 📞 Поддръжка

За въпроси и проблеми, моля отвори issue в GitHub repository.

