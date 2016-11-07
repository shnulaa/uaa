#! /bin/bash
sudo kill $(ps aux | grep 'java' | awk '{print $2}')
sudo kill $(ps aux | grep 'java' | awk '{print $2}')
