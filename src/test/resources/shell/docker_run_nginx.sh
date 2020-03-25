#!/bin/bash
set -x #echo on

export SERVICE_NAME=otaibe-nginx-with-eureka-demo
export WORKDIR=/tmp/$SERVICE_NAME
mkdir -p $WORKDIR
curl -o $WORKDIR/default.conf http://localhost:24456/nginx/config

docker run -i --rm -p 24457:24457 --network host \
          --name=SERVICE_NAME \
          --volume=$WORKDIR:/etc/nginx/conf.d \
          nginx:1.16
