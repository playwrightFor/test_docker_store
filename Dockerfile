# Базовый образ с Java 17 и Maven
FROM maven:3.9.6-eclipse-temurin-17 AS base

# Установка полного набора зависимостей для Playwright
RUN apt-get update && apt-get install -y \
    libnss3 \
    libnspr4 \
    libdbus-1-3 \
    libatk1.0-0 \
    libatk-bridge2.0-0 \
    libatspi2.0-0 \
    libx11-6 \
    libxcomposite1 \
    libxdamage1 \
    libxext6 \
    libxfixes3 \
    libxrandr2 \
    libgbm-dev \
    libdrm-dev \
    libxcb1 \
    libxkbcommon0 \
    libasound2 \
    libharfbuzz-icu0 \
    libpangocairo-1.0-0 \
    xvfb \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Кэширование зависимостей Maven
COPY pom.xml .
RUN mvn dependency:go-offline -B --no-transfer-progress

# Копирование исходного кода
COPY src ./src

# Установка браузеров Playwright через Java API
RUN mvn exec:java -Dexec.mainClass="com.microsoft.playwright.CLI" -Dexec.args="install --with-deps chromium" -B

# Запуск тестов с виртуальным дисплеем
CMD ["sh", "-c", "xvfb-run -a mvn test -B --no-transfer-progress"]


