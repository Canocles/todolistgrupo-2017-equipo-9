#!/bin/sh
sudo docker run --rm  -it -v "${PWD}:/code" -p 80:9000 domingogallardo/playframework
