LABEL maintainer="cjvirtucio87"
LABEL version="1.0"
LABEL description="Dockerfile for running a containerized TusSpringBoot application."

ARG APP_CLIENT_DIR=/app_client
ARG APP_SERVER_DIR=/app_server 
ARG MVN_DOCKER_SETTINGS_DIR=/usr/share/maven/ref/settings-docker.xml

FROM node:latest AS app_client
WORKDIR $APP_CLIENT_DIR
COPY src/client/ .
RUN npm install
RUN npm run build

FROM maven:latest AS app
WORKDIR $APP_SERVER_DIR 
COPY pom.xml .
RUN mvn -B -f pom.xml \
    -s $MVN_DOCKER_SETTINGS_DIR \ 
    dependency:resolve
COPY src/main/java/ src/main/java/
COPY --from=app_client $APP_CLIENT_DIR/build/ src/main/resources/public/
RUN mvn -B -s /usr/share/maven/ref/settings-docker.xml \
    package

FROM java:8-jdk-alpine
WORKDIR /app
COPY --from=app $APP_SERVER_DIR/target/tus-spring-boot-1.0-SNAPSHOT.jar .

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/tus-spring-boot-1.0-SNAPSHOT.jar"]
