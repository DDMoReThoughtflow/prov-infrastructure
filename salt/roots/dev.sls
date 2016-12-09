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
dev-deps:
  pkg.installed:
    - pkgs:
      - java-1.8.0-openjdk
      - java-1.8.0-openjdk-devel

build-services:
  cmd.run:
    - name: /vagrant/gradlew clean build -x test
    - cwd: /vagrant/

renoir-stop:
  service.dead:
    - name: renoir-service

gladys-stop:
  service.dead:
    - name: gladys-service

potter-stop:
  service.dead:
    - name: potter-service

prudence-stop:
  service.dead:
    - name: prudence-service

kev-stop:
  service.dead:
    - name: kev-service

arthur-stop:
  service.dead:
    - name: arthur-service

zita-stop:
  service.dead:
    - name: zita-service

services-remove:
  pkg.purged:
    - pkgs:
      - renoir-service
      - gladys-service
      - potter-service
      - prudence-service
      - kev-service
      - arthur-service
      - zita-service

services-install:
  pkg.installed:
    - sources:
      - renoir-service: /vagrant/renoir-service/build/distributions/renoir-service-0.1.0.SNAPSHOT-1.noarch.rpm
      - gladys-service: /vagrant/gladys-service/build/distributions/gladys-service-0.1.0.SNAPSHOT-1.noarch.rpm
      - potter-service: /vagrant/potter-service/build/distributions/potter-service-0.1.0.SNAPSHOT-1.noarch.rpm
      - prudence-service: /vagrant/prudence-service/build/distributions/prudence-service-0.1.0.SNAPSHOT-1.noarch.rpm
      - kev-service: /vagrant/kev-service/build/distributions/kev-service-0.1.0.SNAPSHOT-1.noarch.rpm
      - arthur-service: /vagrant/arthur-service/build/distributions/arthur-service-0.1.0.SNAPSHOT-1.noarch.rpm
      - zita-service: /vagrant/zita-service/build/distributions/zita-service-0.1.0.SNAPSHOT-1.noarch.rpm


renoir-start:
  service.running:
    - name: renoir-service
    - enable: True

gladys-start:
  service.running:
    - name: gladys-service
    - enable: True

potter-start:
  service.running:
    - name: potter-service
    - enable: True

prudence-start:
  service.running:
    - name: prudence-service
    - enable: True

zita-start:
  service.running:
    - name: zita-service
    - enable: True

kev-start:
  service.running:
    - name: kev-service
    - enable: True

arthur-start:
  service.running:
    - name: arthur-service
    - enable: True
