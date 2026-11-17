# @file Dockerfile
# @brief Imagen del microservicio Question Service (:8083).
# @author Santiago Caicedo
#
# Compilar antes: mvn -pl question-service clean package -DskipTests
# Construir:      docker build -t saberpro-question-service ./question-service

FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Copia el .jar ejecutable generado por el Spring Boot Maven Plugin.
COPY target/*.jar app.jar

# Puerto del microservicio.
EXPOSE 8083

ENTRYPOINT ["java", "-jar", "app.jar"]
