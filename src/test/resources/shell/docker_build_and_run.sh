#!/bin/bash
set -x #echo on

mvn clean package -Pnative -Dquarkus.native.container-build=true

docker build -f src/main/docker/Dockerfile.native -t triphon/otaibe-nginx-with-eureka-demo-config-producer .

docker run -i --rm -p 24456:24456 --network host \
  --name otaibe-nginx-with-eureka-demo-config-producer \
  triphon/otaibe-nginx-with-eureka-demo-config-producer
