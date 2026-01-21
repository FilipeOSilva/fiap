# ===== STAGE 1: BUILD =====
FROM maven:3.8.7-eclipse-temurin-17 AS build

WORKDIR /app

# Copia arquivos de dependência primeiro (melhora cache)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copia o código da aplicação
COPY src ./src

# Build da aplicação
RUN mvn clean package -DskipTests

# ===== STAGE 2: RUNTIME =====
FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=build /app/target/tc01-0.0.1-SNAPSHOT.jar /app/app.jar

EXPOSE 8080


CMD ["java", "-jar", "app.jar"]