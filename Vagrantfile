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

# -*- mode: ruby -*-
# vi: set ft=ruby :

#
# Copyright 2016, Mango Solutions Ltd - All rights reserved.
#
# SPDX-License-Identifier:   AGPL-3.0
#


# All Vagrant configuration is done below. The "2" in Vagrant.configure
# configures the configuration version (we support older styles for
# backwards compatibility). Please don't change it unless you know what
# you're doing.
Vagrant.configure(2) do |config|
  # The most common configuration options are documented and commented below.
  # For a complete reference, please see the online documentation at
  # https://docs.vagrantup.com.

  # Every Vagrant development environment requires a box. You can search for
  # boxes at https://atlas.hashicorp.com/search.
  config.vm.box = "puppetlabs/centos-6.6-64-nocm"

  # Disable automatic box update checking. If you disable this, then
  # boxes will only be checked for updates when the user runs
  # `vagrant box outdated`. This is not recommended.
  # config.vm.box_check_update = false

  # Create a forwarded port mapping which allows access to a specific port
  # within the machine from a port on the host machine. In the example below,
  # accessing "localhost:8080" will access port 80 on the guest machine.
  # default nginx port
  config.vm.network "forwarded_port", guest: 80, host: 80
  # default kafka port
  config.vm.network "forwarded_port", guest: 9092, host: 9092
  # default Zookeeper port
  config.vm.network "forwarded_port", guest: 2181, host: 2181
  # default mongodb port
  config.vm.network "forwarded_port", guest: 27017, host: 27017
  # defualt tomcat port
  config.vm.network "forwarded_port", guest: 8080, host: 8081
  # default arthur port
  config.vm.network "forwarded_port", guest: 10000, host: 10100
  # default renoir port
  config.vm.network "forwarded_port", guest: 10010, host: 10110
  # default gladys port
  config.vm.network "forwarded_port", guest: 10020, host: 10120
  # default potter port
  config.vm.network "forwarded_port", guest: 10030, host: 10130
  # default prudence port
  config.vm.network "forwarded_port", guest: 10040, host: 10140
  # default kev port
  config.vm.network "forwarded_port", guest: 10050, host: 10150
  # default zita port
  config.vm.network "forwarded_port", guest: 10060, host: 10160

  # Create a private network, which allows host-only access to the machine
  # using a specific IP.
  # config.vm.network "private_network", ip: "192.168.33.10"

  # Create a public network, which generally matched to bridged network.
  # Bridged networks make the machine appear as another physical device on
  # your network.
  # config.vm.network "public_network"

  # Share an additional folder to the guest VM. The first argument is
  # the path on the host to the actual folder. The second argument is
  # the path on the guest to mount the folder. And the optional third
  # argument is a set of non-required options.
  # config.vm.synced_folder "../data", "/vagrant_data"

  # Provider-specific configuration so you can fine-tune various
  # backing providers for Vagrant. These expose provider-specific options.
  # Example for VirtualBox:
  #
  config.vm.provider "virtualbox" do |vb|
    # Display the VirtualBox GUI when booting the machine
    # vb.gui = true
    # Customize the amount of memory on the VM:
    vb.memory = "4096"
    vb.cpus = "2"
    vb.customize ["modifyvm", :id, "--ioapic", "on"]
  end
  #
  # View the documentation for the provider you are using for more
  # information on available options.

  # Define a Vagrant Push strategy for pushing to Atlas. Other push strategies
  # such as FTP and Heroku are also available. See the documentation at
  # https://docs.vagrantup.com/v2/push/atlas.html for more information.
  # config.push.define "atlas" do |push|
  #   push.app = "YOUR_ATLAS_USERNAME/YOUR_APPLICATION_NAME"
  # end

  # Enable provisioning with a shell script. Additional provisioners such as
  # Puppet, Chef, Ansible, Salt, and Docker are also available. Please see the
  # documentation for more information about their specific syntax and use.
  config.vm.provision "shell", inline: <<-SHELL
    curl -L https://bootstrap.saltstack.com | sudo sh -s -- stable
  SHELL

  config.vm.synced_folder "salt/roots/", "/srv/salt/"

  config.vm.provision :salt do |salt|
    salt.bootstrap_options = "-F -c /tmp -P"
    salt.minion_config = "salt/minion.yml"
    salt.run_highstate = true
    salt.colorize = true
    salt.log_level = 'debug'
  end

end
