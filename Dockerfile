# Paso 1: Usar una imagen oficial de Maven con Java 17
FROM maven:3.8.5-openjdk-17 AS build

# Creamos un directorio de trabajo neutro en el servidor Linux
WORKDIR /app

# Copiamos solo el contenido de la carpeta interna 'demo' a la raíz del contenedor
COPY demo/ .

# Ejecutamos la compilación limpia sin problemas de rutas
RUN mvn clean package -DskipTests

# Paso 2: Ejecutar la aplicación
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]