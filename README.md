# TusSpringBoot
##### A File Upload service built on Spring Boot and React-Redux, implementing the [TUS Protocol](http://tus.io/).

Make sure you have the latest versions of Maven and the JDK.

Clone this repo.

To run the application, run `./init.sh`. The application will be hosted on `localhost:8080`. Upload a file, then close your browser (or even restart your machine!) before it hits 100%. When you try to upload the same file, the upload process will pick up where it left off.

#### CURRENT FEATURES

- Chunk-based upload.
- Toggle between single stream and chunked uploads.
- Progress table.
- Fault-tolerant; can resume upload even after restarting your machine.
- Containerization.

#### TODOs

- Checksum for data integrity
- Scheduled file cleanup
- Authentication
- Persisting historical data about uploads
- Deployment
- ~~Docker integration.~~
- ~~Unit Tests.~~
- ~~Refactor most logic into a singleton.~~
