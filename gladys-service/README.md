# DDMoRe Workflow Gladys

## Overview
Spring Boot / Spring Integration application for receiving repository notifications
on Kafka topics and resolving them into detailed change descriptions.

## Implementation
The application receives the notification on the `webhook-event` topic and
processes the Git push notifications creating detailed change sets. It then
posts the detailed information onto the vcs-event topic. The default port
for the web management interface is `10020`.

## Building
Build script uses Gradle 2.10. To build you can use the gradle wrapper with the following command.
```sh
$ gradlew build
```

## Running
Spring boot packages the application as an uber jar.
```sh
$ java -jar gladys-1.0.jar
```
