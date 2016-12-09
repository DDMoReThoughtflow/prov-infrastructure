# DDMoRe Workflow Repository Notification Receiver (ReNoiR)

## Overview
Spring Boot / Spring Integration gateway application for receiving repository notifications
via webhooks.

## Implemantation
The application receives the notification from the repository on a URL where the
path is `/hooks/git` for example `http://localhost:10010/hooks/git`. Internally
the application puts these request through a pipeline transforming and unifying
the messages into a message that is then placed onto a message bus (in this
instance Apache Kafka). Note that the default port for this service is `10010`.


### Input message
The application has support for webhooks from GitHub, GitLab and Bitbucket Git
servers. Due to the differences in format and the limited amount of information
that is guaranteed to be in the message the output message is a small subset
of the information given to it.

### Output message
The output message is a JSON formatted message with the following fields:

| Key | Description |
| -- | -- |
| `vcs-repo` | The url to the version control repository |
| `vcs-latest-commit` | The id of the latest commit in the change |
| `vcs-previous-commit` | The commit prior to the batch of commits that lead up to the latest commit. Note that there may be commits in between the previous commit and latest commit, this can be null or undefined.|
| `vcs-branch-name` | The name of the branch that this commit was made against. |
| `vcs-server-implementation` | The implemention of the git server. Current values are either `github`, `gitlab` or `bitbucket` |
| `vcs-event` | Type of event from the version control system. At the moment the only value is `push` |
| `vcs-type` | The type of version control system, currently only `git` |
| `vcs-original-payload` | A String value for the original payload that was included. This makes no guarantee of the format of the payload content and comes straight from the originating source hook. |
| `vcs-original-headers` | The original headers received from the web hook as a JSON formatted map. |

## Building
Build script uses Gradle 2.10. To build you can use the gradle wrapper with the following command.
```sh
$ gradlew build
```

## Running
Spring boot packages the application as an uber jar.
```sh
$ java -jar renoir-1.0.jar
```

Alternatively an RPM is created and installs the application as a service.
