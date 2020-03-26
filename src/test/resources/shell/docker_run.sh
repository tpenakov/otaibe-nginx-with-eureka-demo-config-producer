#!/bin/bash
echo "=================================================="
echo "Usage (from project dir):"
echo "bash src/test/resources/shell/docker_run.sh <EUREKA_HOST_PORT> <HOST_NAME_OR_IP>"
echo
echo "Example:"
echo 'bash src/test/resources/shell/docker_run.sh 172.94.14.97:24455 172.94.14.97'
echo "=================================================="

set -x #echo on


docker run -i -d --rm -p 24456:24456 --network host \
  --name otaibe-nginx-with-eureka-demo-config-producer \
   --env "EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://$1/eureka/" \
   --env "EUREKA_INSTANCE_HOSTNAME=$2" \
  triphon/otaibe-nginx-with-eureka-demo-config-producer
