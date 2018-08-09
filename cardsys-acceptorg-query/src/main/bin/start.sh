#!/bin/sh


PR_PATH=`pwd`


nohup java  -Xms1024M -Xmx1024M -XX:MaxPermSize=128M  -jar  bin/CardOrgQueryServer.jar   --spring.config.location=file:conf/application.properties  &
