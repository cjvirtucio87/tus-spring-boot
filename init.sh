#!/usr/bin/env bash

which docker > /dev/null

if [ $? = 0 ]; then
  echo "Docker detected. Building image."
  cp $M2_HOME/conf/settings.xml .
  docker build \
      --build-arg HTTP_PROXY=$HTTP_PROXY \
      --build-arg HTTP_PROXY_PORT=$HTTP_PROXY_PORT \
      -t tus-spring-boot .
  echo "Done."
  echo "Running tus-spring-boot. Cheers!"
  rm settings.xml
  docker run -p 8080:8080 -t --name=tus-spring-boot tus-spring-boot
else
  echo "Docker not detected." 
  echo "Running the app with good ol' java. Enjoy!"
  mvn clean install && java -jar target/tus-spring-boot-1.0-SNAPSHOT.jar
fi
