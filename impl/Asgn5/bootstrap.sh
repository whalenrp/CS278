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

# install jython
  sudo apt-get install -y curl
  curl http://search.maven.org/remotecontent?filepath=org/python/jython-installer/2.7-b1/jython-installer-2.7-b1.jar -O jython-installer-2.7-b1.jar
  sudo java -jar jython-installer-2.7-b1.jar -s -d /usr/lib/jvm/jython
  sudo echo "JYTHON_HOME=/usr/lib/jvm/jython" | sudo tee -a /etc/environment
  export JYTHON_HOME=/user/lib/jvm/jython
  source /etc/environment
  cp $JYTHON_HOME/jython.jar /home/vagrant/share/ReceiptListServer/libs/

set -e

cd /vagrant/ReceiptListServer
ant all
#cd bin; java ReceiptListServer
