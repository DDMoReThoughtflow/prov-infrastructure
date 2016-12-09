# Provenance Workflow Infrastructure

## Overview
This project contains all of the infrastructure and background services that
form part of the Provenance Workflow Infrastructure. The services sit on a
messaging backbone implemented using Apache Kafka to send messages between
services.

## Building
### Requirements
* Java 1.7 or above.

### Building
The project uses the gradle build system and contains the gradle wrapper script
which will automatically obtain the specified version of gradle. Building the
software for the first time maybe slow.

#### Build commands
* Build the whole project: `gradlew build`
* Cleaning the project: `gradlew clean`
* Build a specific sub module e.g. renoir-service: `gradlew :renoir-service:build`
* Clean a specific sub module e.g. gladys-service: `gradlew :gladys-service:clean`

#### Contributing
Contribution of code to this project should follow the following rules:
* This repository follows general [Git Flow](https://www.atlassian.com/git/tutorials/comparing-workflows/gitflow-workflow) development practices using feature branches.
* All development should be done on feature branches and merged back into the develop branch.
* Feature branches should not be long lived and should be used to develop small
changes towards a common goal.
* The master branch should always contain working source code.


#### IDE integration
#### Eclipse
There are 2 mechanisms to load the project into the Eclipse IDE:
1. Use the Eclipse gradle plugin [BuildShip](https://github.com/eclipse/buildship), this is an eclipse plugin that understands gradle projects.
2. Use the gradle eclipse plugin (this is a plugin in the gradle build file that will generate the appropriate eclipse project). To generate the eclipse files run the following `gradlew eclipse`, you can then import the project in as an existing project. When you add a new dependency in you will need to run this again to regenerate the eclipse project files and then refresh the project in eclipse and it will pick up the new settings. If you want to just generate the eclipse files for a specific sub module then run the command `gradlew :store:eclipse` for the store sub project.

## Components

### Apache Kafka
This is the generic high performance message backbone that underpins the
asynchronous messages used by the provenance infrastructure.

For zookeeper and kafka installation instructions:
* [Kafka Install Instructions](http://www.cloudera.com/documentation/kafka/latest/topics/kafka_installing.html)
* [Zookeeper Install Instructions](http://www.cloudera.com/documentation/archive/cdh/4-x/4-2-0/CDH4-Installation-Guide/cdh4ig_topic_21_3.html)

#### Topics
Kafka consists of multiple topics that the subscribers can receive notifications
on. The topics configured are:

* webhook: Topic to post webhook events.
* vcs: Topic for version control events.
* provenance: Topic for publishing provenance information.
* notifications: general notifications topic.
* log: topic for publishing logs to.

Consuming a topic on the commandline to check the contents of the messages can
be done with the following command:
```
kafka-console-consumer --zookeeper localhost:2181 --topic publish
```

### Apache Zookeeper
This is a service required by Apache Kafka and is used for service registration
and discovery.

### Renoir
Receives webhook events, currently from git repositories and publishes these
events onto the webhook-event topic.

### Gladys
Translates webhook vcs events into concrete VCS events and posts them onto the
vcs-event topic.

### Potter
Converts events into provenance documents through configured templates and posts
them onto the prov-payload topic.

### Prudence
Receives provenance documents and uploads them to provstore.

### Arthur
Spring boot admin console, not a required part of the infrastructure so is not
installed as part of the vagrant machine by default.

### Kev
Exposes web interface for posting messages onto kafka topics.

### Store
This is a sub module with multiple submodules  and is the provenance store
implementation. See the README.md file in the root of this project for details.

## Infrastructure

### Vagrant
The project contains a Vagrant virtual machine definition that commissions a machine with the following:
* Apache Zookeeper - single host single broker setup.
* Apache Kafka - single host with the topics specified above.
* All of the required service modules within the project.

To start the virtual machine use `vagrant up`. Note that this can take a long time
on initial startup as it will need to download the base image and then commission
the software on the machine.

### Kafka Offest Monitor
A useful utility for monitoring the kafka topic lag is [Kafka Offset Monitor](https://github.com/quantifind/KafkaOffsetMonitor). You will need to
download the application and then use the following commandline:

```
java -cp KafkaOffsetMonitor-assembly-0.2.1.jar com.quantifind.kafka.offsetapp.OffsetGetterWeb --zk localhost  --port 8080 --refresh 10.seconds --retain 2.days
```
## LICENSE & Copyright

Copyright 2016, Mango Solutions Ltd - All rights reserved.

SPDX-License-Identifier:   AGPL-3.0



## TODO
* Add basic security onto the services.
* Update the configuration for generating the rpms.
  * Add an installation user.
* Document installation and configuration options.
* Implement error handling strategy.
* Add data version information onto the messages/payloads so that if the
messages change then the services can route the requests appropriately.
* Unify the settings across all applications.
* Register the apps with zookeeper.
* Look at the kafka queue consumer configuration.
* Need to update the message-key values on the producer as these determine the
partition the message ends up on.
* Need to hook all of the kafka consumers into a payload flattener in a generic
way.
* Need to define a generic packet for the payloads and meta information.
* Once have the above need to add in tracking information to the requests.
* Change the logger to log in JSON format.
* Change the logger to log to a logging topic.
* Add notifications into the system.

### Renoir
* Create unit tests.
* Add support for secret key from web hooks.
* Document the customisation options.
* Need to cope with the previous commit being null.

### Gladys
* Currently can only pull public repositories.
* Needs to cache the repositories, currently pulls them fresh each time.
* Need to cope with the previous commit being null which indicates back to the
original commit.
