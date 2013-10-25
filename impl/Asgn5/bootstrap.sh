#!/usr/bin/env bash


set -e

cd /vagrant/ReceiptListServer
ant all
cd bin; java ReceiptListServer
