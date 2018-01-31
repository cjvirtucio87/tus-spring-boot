#!/usr/bin/env bash

client_dir=src/client
jar_path=target/tus-spring-boot-1.0-SNAPSHOT.jar
settings=$M2_HOME/conf/settings.xml

function get_client {
  git clone git@github.com:cjvirtucio87/tus-spring-boot-client.git $client_dir 
}

function build_client {
	pushd $client_dir
	npm install && \
    npm run build && \
		popd && \
		mv $client_dir/build/ src/main/resources/public/
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

function build_app {
  local opt=$1

  case $opt in
    "--help" )
      print_help 
      exit 0 ;;
    "--clean-mvn" )
      mvn clean install ;;
    "--clean-client" )
      build_client && mvn install ;;
    "--clean-all" )
      build_client && mvn clean install ;;
    "" )
      echo "No option passed. Running the application." ;;
    * )
      echo "Error! Invalid argument!"
      exit 1;
  esac
}

function run_app {
	java -jar $jar_path
}

function print_help {
  local msg="This is the init script for the TusSpringBoot project."
  msg="$msg\n[OPTIONS]"
  msg="$msg\n--help (print this help message)"
  msg="$msg\n--clean-mvn (run the app with the 'clean' argument for maven)"
  msg="$msg\n--clean-client (run the app with a fresh production build of the reactjs client)"
  msg="$msg\n--clean-all (run the app with a fresh build of both the server and the client)"
  printf "$msg\n"
}

function main {
  local opt=$1
 
  echo "Downloading client."
  get_client
  if [ $? = 0 ]; then 
    echo "Done."
  else
    echo "Error! Failed to download client!"
    exit 1
  fi

  which docker > /dev/null
  
  if [ $? != 0 ]; then
    echo "Docker detected." 
    echo "Building image."
    build_image
    
    if [ $? = 0 ]; then
      echo "Done."
      echo "Running tus-spring-boot. Cheers!"
      rm settings.xml
      rm -rf $client_dir 
      run_container
    else
      echo "Error! Failed to build the image."
      exit 1
    fi
  else
    echo "Docker not detected."
    
    if [ ! -d "src/main/resources/public" ]; then
      if [ ! -f "$jar_path" ]; then
        echo "Creating build for first-time run."
        opt="--clean-all"
      elif [ "$opt" == "--clean-mvn" ]; then
        echo "No client code in the resources folder. Building client code, first."
        opt="--clean-all"
      fi
    fi

    build_app $opt
    
    if [ $? = 0 ]; then
      echo "Done."
      echo "Running tus-spring-boot. Enjoy!"
  		rm -rf $client_dir 
      rm -rf src/main/resources/public
      run_app
    else
      echo "Error! Failed to build the application."
      exit 1
    fi
  fi
}

main $1
