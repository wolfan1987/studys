#!/bin/sh 
proc=bin/CardOrgQueryServer.jar 
echo "$proc exists or not......"
    
echo `ps |grep $proc`     
    
proc_id=`ps -ef|grep -v grep|grep $proc|awk '{print $2}'`  
if [ -n "$proc_id" ] 
then 
   echo " $proc  is  running,now kill......" 
   kill -9 $proc_id
   rm  -rf  nohup.out 
   free
   echo "$proc killed  ok !"     
else 
   echo "$proc is not running!" 
fi 
