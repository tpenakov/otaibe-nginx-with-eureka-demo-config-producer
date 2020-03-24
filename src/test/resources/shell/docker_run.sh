#!/bin/bash
set -x #echo on

docker run -i --rm -p 24456:24456 --network host \
  --name otaibe-nginx-with-eureka-demo-config-producer \
  triphon/otaibe-nginx-with-eureka-demo-config-producer
