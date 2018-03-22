# TusSpringBoot
##### A File Upload service built on Spring Boot and React-Redux, implementing the [TUS Protocol](http://tus.io/).

### REQUIREMENTS

* docker (if you want it to run as a container)
* nodejs and NPM (if you don't have docker)
* JDK 8
* maven

### USAGE

Clone this repo.

This application is initialized using the `./init.sh` script. Run `./init.sh --help` to see the available options.

Whether containerized or on the host OS, the application will be available on `localhost:8080`. Upload a file, then close your browser (or even restart your machine!) before it hits 100%. When you try to upload the same file, the upload process will pick up where it left off.

_NOTE: If you're behind a corporate firewall, set the HTTP_PROXY and HTTP_PROXY_PORT environment variables_

### CURRENT FEATURES

- Chunk-based upload.
- Toggle between single stream and chunked uploads.
- Progress table.
- Fault-tolerant; can resume upload even after restarting your machine.
- Containerization.

### TODOs

- Checksum for data integrity
- Scheduled file cleanup
- Authentication
- Persisting historical data about uploads
- Deployment
- ~~Docker integration.~~
- ~~Unit Tests.~~
- ~~Refactor most logic into a singleton.~~
