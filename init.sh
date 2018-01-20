#!/usr/bin/env bash

which docker > /dev/null

if [ $? = 0 ]; then
  echo "Docker detected. Building image."
  docker build -t tus-spring-boot .
  echo "Done."
  echo "Running tus-spring-boot. Cheers!"
  docker run -p 8080:8080 -t --name=tus-spring-boot tus-spring-boot
else
  echo "Docker not detected." 
  echo "Running the app with good ol' java. Enjoy!"
  mvn clean install && java -jar target/tus-spring-boot-1.0-SNAPSHOT.jar
fi
