#!/usr/bin/env bash
activator -jvm-debug 10011 -Dhttp.port=10010 -Dconfig
.file=/Users/Luong/Documents/repos/templates/play-mongo/conf/application.conf run
