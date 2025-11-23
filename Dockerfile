# Build stage
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copy common module first and install it
COPY senpai-common/pom.xml /common/pom.xml
COPY senpai-common/src /common/src
WORKDIR /common
RUN mvn clean install -DskipTests

# Copy project files
WORKDIR /app
COPY senpai-main/senpai-main/pom.xml .
RUN mvn dependency:go-offline -B

COPY senpai-main/senpai-main/src ./src

# Build the application
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy the built JAR
COPY --from=build /app/target/*.jar app.jar

# Create directory for uploads
RUN mkdir -p /app/uploads

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]

