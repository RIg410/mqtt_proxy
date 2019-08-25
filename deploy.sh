#!/usr/bin/env bash
ssh pi@192.168.0.100 'sudo systemctl stop mqtt-proxy'
scp ./build/libs/mqtt_proxy-1.0-SNAPSHOT.jar pi@192.168.0.100:/home/pi/jane/mqtt_proxy.jar
ssh pi@192.168.0.100 'sudo systemctl start mqtt-proxy'

