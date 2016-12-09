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
mongodb-org-3.2:
  pkgrepo.managed:
    - humanname: MongoDB Repository
    - baseurl: https://repo.mongodb.org/yum/redhat/$releasever/mongodb-org/3.2/x86_64/
    - gpgcheck: 0
    - disabled: 0

mongodb:
  pkg.installed:
    - pkgs:
      - mongodb-org
  service.running:
    - name: mongod
    - enable: True

# Setting mongodb to listen on all network interfaces.
/etc/mongod.conf:
  file.comment:
    - regex: "  bindIp: 127.0.0.1.*"
