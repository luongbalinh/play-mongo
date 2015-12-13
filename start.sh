#!/usr/bin/env bash
activator -jvm-debug 10011 -Dhttp.port=9000 -Dconfig.file=conf/application.test.conf
