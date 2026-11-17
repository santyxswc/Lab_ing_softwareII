# @file Dockerfile
# @brief Imagen del microservicio Validation Service (:8082).
# @author Santiago Caicedo
#
# Compilar antes: mvn -pl validation-service clean package -DskipTests
# Construir:      docker build -t saberpro-validation-service ./validation-service

FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Copia el .jar ejecutable generado por el Spring Boot Maven Plugin.
COPY target/*.jar app.jar

# Puerto del microservicio.
EXPOSE 8082

ENTRYPOINT ["java", "-jar", "app.jar"]
