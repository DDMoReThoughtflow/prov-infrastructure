# DDMoRe Workflow Prudence

## Overview
Spring Boot / Spring Integration application for receiving providence
information and uploading them to the providence store.

## Implementation
The application receives the notification on the `prov-payload` topic and
processes the notification. The default port for the web management interface
is `10040`.

## Building
Build script uses Gradle 2.10. To build you can use the gradle wrapper with the following command.
```sh
$ gradlew build
```

## Running
Spring boot packages the application as an uber jar.
```sh
$ java -jar prudence-1.0.jar
```
