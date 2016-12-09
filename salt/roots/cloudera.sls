################################################################################
# Copyright (C) 2016 Mango Business Solutions Ltd, [http://www.mango-solutions.com]
#
# This program is free software: you can redistribute it and/or modify it under
# the terms of the GNU Affero General Public License as published by the
# Free Software Foundation, version 3.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
# or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
# for more details.
#
# You should have received a copy of the GNU Affero General Public License along
# with this program. If not, see <http://www.gnu.org/licenses/agpl-3.0.html>.
################################################################################


#sudo salt-call --local state.highstate
cloudera-cdh5:
  pkgrepo.managed:
    - humanname: Cloudera's Distribution for Hadoop, Version 5
    - baseurl: https://archive.cloudera.com/cdh5/redhat/6/x86_64/cdh/5/
    - gpgcheck: 0
    - gpgkey: https://archive.cloudera.com/cdh5/redhat/6/x86_64/cdh/RPM-GPG-KEY-cloudera
    - disabled: 0

cloudera-kafka-2:
  pkgrepo.managed:
    - humanname: Cloudera's Distribution for kafka, Version 2
    - baseurl: http://archive.cloudera.com/kafka/redhat/6/x86_64/kafka/2/
    - gpgcheck: 0
    - gpgkey:  http://archive.cloudera.com/kafka/redhat/6/x86_64/kafka/RPM-GPG-KEY-cloudera
    - disabled: 0

#Need to install these twice as seems like an error in the Cloudera repository the first download always thinks that it has failed.
cloudera-pkgs-1:
  pkg.installed:
    - pkgs:
      - zookeeper-server
      - kafka-server

cloudera-pkgs-2:
  pkg.installed:
    - pkgs:
      - zookeeper-server
      - kafka-server

zookeeper-server:
  cmd.run:
    - name: service zookeeper-server init
    - creates: /var/lib/zookeeper/version-2
  service.running:
    - enable: True

kafka-server:
  service.running:
  - enable: True

kafka-test-topic:
  cmd.run:
    - name: /usr/bin/kafka-topics --create --zookeeper localhost:2181 --replication-factor 1 --partitions 3 --topic test
    - unless: /usr/bin/kafka-topics --list --zookeeper localhost:2181 | grep test

kafka-notifications-topic:
  cmd.run:
    - name: /usr/bin/kafka-topics --create --zookeeper localhost:2181 --replication-factor 1 --partitions 3 --topic notifications
    - unless: /usr/bin/kafka-topics --list --zookeeper localhost:2181 | grep notifications

kafka-webhook-event-topic:
  cmd.run:
    - name: /usr/bin/kafka-topics --create --zookeeper localhost:2181 --replication-factor 1 --partitions 3 --topic webhook
    - unless: /usr/bin/kafka-topics --list --zookeeper localhost:2181 | grep webhook

kafka-vcs-change-topic:
  cmd.run:
    - name: /usr/bin/kafka-topics --create --zookeeper localhost:2181 --replication-factor 1 --partitions 3 --topic vcs
    - unless: /usr/bin/kafka-topics --list --zookeeper localhost:2181 | grep vcs

kafka-prov-payload-topic:
  cmd.run:
    - name: /usr/bin/kafka-topics --create --zookeeper localhost:2181 --replication-factor 1 --partitions 3 --topic provenance
    - unless: /usr/bin/kafka-topics --list --zookeeper localhost:2181 | grep provenance

kafka-publish-topic:
  cmd.run:
    - name: /usr/bin/kafka-topics --create --zookeeper localhost:2181 --replication-factor 1 --partitions 3 --topic activityrecords
    - unless: /usr/bin/kafka-topics --list --zookeeper localhost:2181 | grep activityrecords

kafka-logs-topic:
  cmd.run:
    - name: /usr/bin/kafka-topics --create --zookeeper localhost:2181 --replication-factor 1 --partitions 3 --topic log
    - unless: /usr/bin/kafka-topics --list --zookeeper localhost:2181 | grep log
