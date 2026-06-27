# Paso 1: Construir usando una versión estable de Maven sobre Alpine (arregla problemas de red/SSL)
FROM maven:3.8.6-eclipse-temurin-17-alpine AS build
COPY . .
WORKDIR /demo
# Ejecutamos el empaquetado omitiendo pruebas
RUN mvn clean package -DskipTests

# Paso 2: Ejecutar la aplicación
FROM eclipse-temurin:17-jdk-jammy
COPY --from=build /demo/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]