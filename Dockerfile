# Paso 1: Construir la aplicación usando Maven y Java 17
FROM maven:3.8.5-openjdk-17 AS build
COPY . .
WORKDIR /demo
RUN mvn clean package -DskipTests

# Paso 2: Ejecutar la aplicación con una imagen de Java activa y oficial
FROM eclipse-temurin:17-jdk-jammy
COPY --from=build /demo/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]