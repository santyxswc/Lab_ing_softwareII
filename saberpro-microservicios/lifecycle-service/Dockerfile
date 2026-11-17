# @file Dockerfile
# @brief Imagen del microservicio Lifecycle Service (:8084).
# @author Santiago Caicedo
#
# Construcción en dos etapas: la primera compila el .jar con Maven y la
# segunda solo lleva Java 17 y el .jar, así la imagen final es liviana y
# no hace falta tener Maven ni Java instalados en la máquina.
#
# El contexto de construcción es la carpeta saberpro-microservicios/ porque
# el módulo hereda del pom.xml agregador. Lo fija docker-compose.yml; a mano:
#   docker build -f lifecycle-service/Dockerfile -t saberpro-lifecycle-service .
#
# Nota sobre la imagen base: la guía pide openjdk:17-jdk-slim, pero Docker Hub
# retiró esa etiqueta (la imagen "openjdk" quedó obsoleta). Se usa su reemplazo
# oficial, Eclipse Temurin 17 (solo el runtime, más liviano). Si la etiqueta original está disponible en otro
# registro, se cambia sin tocar este archivo:
#   docker compose build --build-arg RUNTIME_IMAGE=openjdk:17-jdk-slim
ARG RUNTIME_IMAGE=eclipse-temurin:17-jre-jammy

# ---- Etapa 1: compilar el .jar ejecutable -----------------------------------
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /workspace
COPY pom.xml .
COPY validation-service/pom.xml validation-service/pom.xml
COPY question-service/pom.xml question-service/pom.xml
COPY lifecycle-service/pom.xml lifecycle-service/pom.xml
COPY lifecycle-service/src lifecycle-service/src
RUN --mount=type=cache,target=/root/.m2 \
    mvn -B -q -pl lifecycle-service -am clean package -DskipTests

# ---- Etapa 2: imagen de ejecución -------------------------------------------
FROM ${RUNTIME_IMAGE}
WORKDIR /app
COPY --from=build /workspace/lifecycle-service/target/*.jar app.jar
EXPOSE 8084
ENTRYPOINT ["java", "-jar", "app.jar"]
