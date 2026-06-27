# Paso 1: Usar una imagen oficial de Maven con Java 17
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app

# Como tus archivos ya están sueltos en la raíz de GitHub, copiamos todo directo
COPY . .

# Compilamos limpiamente
RUN mvn clean package -DskipTests

# Paso 2: Ejecutar la aplicación
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]