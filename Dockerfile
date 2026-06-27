# Paso 1: Construir la aplicación usando Maven y Java 17
FROM maven:3.8.5-openjdk-17 AS build
# Copiamos todo el contenido
COPY . .
# Entramos a la carpeta interna donde está el pom.xml realmente
WORKDIR /demo
RUN mvn clean package -DskipTests

# Paso 2: Ejecutar la aplicación
FROM openjdk:17-jdk-slim
# Copiamos el archivo JAR generado desde la carpeta build/demo/target
COPY --from=build /demo/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]