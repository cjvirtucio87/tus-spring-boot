# TusSpringBoot
##### A File Upload service built on Spring Boot and React-Redux, implementing the [TUS Protocol](http://tus.io/).

Run `src/main/java/TusSpringBoot/App` to start the back-end server.

To run the client app, cd into `src/client` and type `npm start`.

The back-end server will be hosted on `localhost:8080`, while the React app will be hosted on `localhost:3000`.

#### CURRENT FEATURES

- Chunk-based upload.
- Toggle between single stream and chunked uploads.
- Progress table.

#### TODOs

- Add event for upload completion
- Add handler for concatenating chunks