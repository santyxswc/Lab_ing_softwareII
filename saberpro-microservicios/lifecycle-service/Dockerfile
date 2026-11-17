# @file Dockerfile
# @brief Imagen del microservicio Lifecycle Service (:8084).
# @author Santiago Caicedo
#
# Compilar antes: mvn -pl lifecycle-service clean package -DskipTests
# Construir:      docker build -t saberpro-lifecycle-service ./lifecycle-service

FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Copia el .jar ejecutable generado por el Spring Boot Maven Plugin.
COPY target/*.jar app.jar

# Puerto del microservicio.
EXPOSE 8084

ENTRYPOINT ["java", "-jar", "app.jar"]
