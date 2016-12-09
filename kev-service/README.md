# DDMoRe Workflow Kev

## Overview
Spring Boot / Spring Integration gateway application for pushing raw
notifications onto any Kafka topic.

## Implementation
The application exposes the end point `http://localhost:10050/topics/post`.
Combined with the header `x-kev-topic` to define the topic to post on to it will
transfer the data. It performs no validation on the content. Default port is
`10050`.


## Building
Build script uses Gradle 2.10. To build you can use the gradle wrapper with the following command.
```sh
$ gradlew build
```

## Running
Spring boot packages the application as an uber jar.
```sh
$ java -jar kev-1.0.jar
```

Alternatively an RPM is created and installs the application as a service.
