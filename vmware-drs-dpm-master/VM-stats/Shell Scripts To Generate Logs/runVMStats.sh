#!/bin/bash


java -jar VMStatsjar.jar &
echo ""
cd /home/t18-vm1/Downloads
jps | grep jar > runVMStatsPID.txt
echo ""
