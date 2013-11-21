#!/usr/bin/env bash
sudo apt-get update

sudo apt-get install -y python-pip
sudo easy_install nose

#Install the newest version of MongoDM  
  mkdir -p /data/db 
  chown -R vagrant /data
  sudo apt-key adv --keyserver keyserver.ubuntu.com --recv 7F0CEB10 #install new version
  echo 'deb http://downloads-distro.mongodb.org/repo/ubuntu-upstart dist 10gen' | sudo tee /etc/apt/sources.list.d/10gen.list
  sudo apt-get update
  sudo apt-get install -y mongodb-10gen
  sudo pip install pymongo


set -e

cd /vagrant/ReceiptListServer
ant all
#cd bin; java ReceiptListServer
