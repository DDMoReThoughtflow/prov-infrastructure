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
nginx-repo:
  pkgrepo.managed:
    - humanname: nginx repo
    - baseurl: http://nginx.org/packages/centos/$releasever/$basearch/
    - gpgcheck: 0
    - disabled: 0


nginx:
  pkg.installed:
    - name: nginx
  file.managed:
    - name: /etc/nginx/conf.d/default.conf
    - source: salt://files/nginx/default.conf
    - user: root
    - group: root
    - makedirs: True
  service.running:
    - enable: True
    - watch:
      - file: /etc/nginx/conf.d/default.conf
