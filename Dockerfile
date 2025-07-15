# Etapa 1: Construir o JAR da aplicação
FROM maven:3.8.6-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Criar a imagem final
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /app/target/spring-clean-arch-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]