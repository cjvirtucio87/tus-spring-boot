#!/usr/bin/env bash

client_dir=src/client
settings=$M2_HOME/conf/settings.xml

function get_client {
  git clone git@github.com:cjvirtucio87/tus-spring-boot-client.git $client_dir 
}

function build_image {
  cp $settings .
  docker build \
    --build-arg HTTP_PROXY=$HTTP_PROXY \
    --build-arg HTTP_PROXY_PORT=$HTTP_PROXY_PORT \
    -t tus-spring-boot .
}

function run_container { 
  docker run -p 8080:8080 -t --name=tus-spring-boot tus-spring-boot
}

function build_client {
	pushd $client_dir
	npm install && \
    npm run build && \
		popd && \
		mv $client_dir/build/ src/main/resources/public/
}

function run_app {
  mvn clean install && \
	  java -jar target/tus-spring-boot-1.0-SNAPSHOT.jar
}

function main {
  which docker > /dev/null
  
  if [ $? = 0 ]; then
    echo "Docker detected." 
    echo "Downloading client."
    get_client
    echo "Done."
    echo "Building image."
    build_image
    echo "Done."
    echo "Running tus-spring-boot. Cheers!"
    rm settings.xml
    rm -rf $client_dir 
    run_container
  else
    echo "Docker not detected." 
    echo "Downloading client."
    get_client
    echo "Done."
    echo "Building client."
  	build_client
    echo "Done."
    echo "Running tus-spring-boot. Enjoy!"
		rm -rf $client_dir
    run_app
    rm -rf src/main/resources/public
  fi
}

main
