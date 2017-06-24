# TusSpringBoot
##### A File Upload service built on Spring Boot and React-Redux, implementing the [TUS Protocol](http://tus.io/).

Clone this repo.

Enter `mvn clean package && java -jar target/TusSpringBoot-1.0-SNAPSHOT.war` to build the war file and run the application.

To run the client app, cd into `src/client` and type `npm start`.

The back-end server will be hosted on `localhost:8080`, while the React app will be hosted on `localhost:3000`.

#### CURRENT FEATURES

- Chunk-based upload.
- Toggle between single stream and chunked uploads.
- Progress table.
- Fault-tolerant; can resume upload even after restarting your machine.

#### TODOs

- Docker integration.
- ~~Unit Tests.~~
- ~~Refactor most logic into a singleton.~~
