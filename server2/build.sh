#!/bin/bash

cd frontend
docker build -f ../Dockerfile -t frontend:1.0.0 .
cd ../loginserver
docker build -f ../Dockerfile -t loginserver:1.0.0 .
cd ../charserver
docker build -f ../Dockerfile -t charserver:1.0.0 .
cd ../chatserver
docker build -f ../Dockerfile -t chatserver:1.0.0 .
cd ../worldserver
docker build -f ../Dockerfile -t worldserver:1.0.0 .
