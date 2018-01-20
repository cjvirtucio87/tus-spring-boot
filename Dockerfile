# from the node image,
# set workdir as src/client
# run npm install 
# run npm run build


FROM node:latest AS assets
WORKDIR /usr/git/cjvirtucio-tus-spring-boot/src/client/
COPY src/client/ .
RUN npm install
RUN npm run build

FROM maven:latest AS app
WORKDIR /usr/git/cjvirtucio-tus-spring-boot/
COPY pom.xml .
RUN mvn -B -f pom.xml \
    -s /usr/share/maven/ref/settings-docker.xml \
    dependency:resolve
COPY src/main/java/ src/main/java/
COPY --from=assets /usr/git/cjvirtucio-tus-spring-boot/src/client/build/ src/main/resources/public/
RUN mvn -B -s /usr/share/maven/ref/settings-docker.xml \
    package

FROM java:8-jdk-alpine
WORKDIR /app
COPY --from=app /usr/git/cjvirtucio-tus-spring-boot/target/tus-spring-boot-1.0-SNAPSHOT.jar .

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/tus-spring-boot-1.0-SNAPSHOT.jar"]
