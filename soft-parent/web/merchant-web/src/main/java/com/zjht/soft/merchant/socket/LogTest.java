package com.zjht.soft.merchant.socket;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by zjhtadmin on 2018/1/4.
 */
public class LogTest {

    private static ArrayBlockingQueue<String>  logQueues = new ArrayBlockingQueue<>(500);
    private static  int count = 0;
    private static Executor  executor  = Executors.newFixedThreadPool(2);
    public static void main(String[] args) throws InterruptedException {
        for(int i=0; i <2 ; i++){
            executor.execute(new LogProcess("线程"+i));
        }
        for(int i = 1; i <=100000; i++){
            logQueues.add("content"+i);
            if(i%200==0){
                Thread.sleep(1000);
            }
        }
    }

    static class   LogProcess implements Runnable{
        private String threadName;
        public  LogProcess(String threadName){
            this.threadName = threadName;
        }
        @Override
        public void run() {
            while(true){
                try {
                    String  content = logQueues.take();
                    System.out.println(threadName+".content="+content);
                    count++;
                    if(count%250==0){
                        throw new  RuntimeException("450");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    continue;
                }catch(Exception  ex){
                    ex.printStackTrace();
                    continue;
                }
            }

        }
    }


}
