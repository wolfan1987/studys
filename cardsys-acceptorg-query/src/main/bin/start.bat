SET PR_PATH=%CD%

java   -server -Xms1024M -Xmx1024M -XX:MaxPermSize=128M  -jar  bin/CardOrgQueryServer.jar   --spring.config.location=file:conf/application.properties