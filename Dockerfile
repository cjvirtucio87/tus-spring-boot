ARG HTTP_PROXY
ARG HTTP_PROXY_PORT

FROM node:latest AS app_client
WORKDIR /app_client
COPY src/client/ .
RUN npm config set proxy $HTTP_PROXY:$HTTP_PROXY_PORT && \
    npm install && \
    npm run build

FROM maven:latest AS app_server
WORKDIR /app_server
COPY settings.xml /usr/share/maven/ref/settings.xml
COPY pom.xml .
RUN mvn -B -f pom.xml \
    -s /usr/share/maven/ref/settings.xml \ 
    dependency:resolve
COPY src/main/java/ src/main/java/
COPY --from=app_client /app_client/build/ src/main/resources/public/
RUN mvn -B -s /usr/share/maven/ref/settings.xml \
    package

FROM java:8-jdk-alpine
WORKDIR /app
COPY --from=app_server /app_server/target/tus-spring-boot-1.0-SNAPSHOT.jar .

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/tus-spring-boot-1.0-SNAPSHOT.jar"]

LABEL maintainer="cjvirtucio87"
LABEL version="1.0"
LABEL description="Dockerfile for running a containerized TusSpringBoot application."
