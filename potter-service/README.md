# DDMoRe Workflow Potter

## Overview
Spring Boot / Spring Integration application for receiving notifications from
various soruces and generating provenance documents based upon templates. This
will post the document onto the prov-payload topic.

## Implementation
The application receives the notification on the `vcs-event` topic and
processes the notification. The default port for the web management interface
is `10030`.

## Building
Build script uses Gradle 2.10. To build you can use the gradle wrapper with the following command.
```sh
$ gradlew build
```

## Running
Spring boot packages the application as an uber jar.
```sh
$ java -jar potter-1.0.jar
```

## TODO
