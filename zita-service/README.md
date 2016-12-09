# DDMoRe Workflow Zita

## Overview
Spring Boot / Spring Integration application for receiving partial providence
information and storing it until it needs to be assembled into a full providence
document.

## Implementation
The application receives the notification on the `provenance` topic and
processes the notification. The default port for the web management interface
is `10060`.

## Building
Build script uses Gradle 2.11. To build you can use the gradle wrapper with the following command.
```sh
$ gradlew build
```

## Running
Spring boot packages the application as an uber jar.
```sh
$ java -jar zita-service-0.1.0-SNAPSHOT.jar
```
