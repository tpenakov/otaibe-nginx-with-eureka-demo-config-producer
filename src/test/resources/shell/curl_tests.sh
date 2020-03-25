#!/bin/bash
#set -x #echo on

for i in {1..15} ; do
  curl http://localhost:24457/rest && echo ''
done