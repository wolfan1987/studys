package com.zjht.soft.merchant.socket;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Created by zjhtadmin on 2017/11/2.
 */
public class WriterTestHexData  extends Thread{

    public  static Deque<String>   payHexQueue = new ConcurrentLinkedDeque<>();
    public  static Deque<String>   cancleHexQueue = new ConcurrentLinkedDeque<>();
    public  static Deque<String>   queryHexQueue = new ConcurrentLinkedDeque<>();
    private static  String  dataFilePath =  "C:\\Users\\zjhtadmin.20151207-153221\\Desktop\\testdata\\";
    private  FileWriter  payFW = null;
    private   BufferedWriter payBW = null;
    private  FileWriter  cancleFW = null;
    private   BufferedWriter cancleBW = null;
    private   FileWriter  queryFW = null;
    private   BufferedWriter queryBW = null;
    private   int  writeCount = 0;
    //初始化3个文件对象
    public  WriterTestHexData(){
        try {
            payFW = new FileWriter(dataFilePath+"payData.txt", true);
            payBW = new BufferedWriter(payFW);
            cancleFW = new FileWriter(dataFilePath+"cancleData.txt", true);
            cancleBW = new BufferedWriter(cancleFW);
            queryFW = new FileWriter(dataFilePath+"queryData.txt", true);
            queryBW = new BufferedWriter(queryFW);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void  run(){
        try {
            sleep(15000);
            System.out.println("--------run-----------");
            while(true){
                String   dataItem =  payHexQueue.pollFirst();
                if(dataItem!=null){
                   payBW.write(dataItem);
                }
                dataItem = cancleHexQueue.pollFirst();
                if(dataItem!=null){
                    cancleBW.write(dataItem);
                }
                dataItem = queryHexQueue.pollFirst();
                if(dataItem!=null){
                    queryBW.write(dataItem);
                }
                if(writeCount>=20){
                    payBW.flush();
                    cancleBW.flush();
                    queryBW.flush();
                    writeCount=0;
                }
                if(payHexQueue.isEmpty() && cancleHexQueue.isEmpty() && queryHexQueue.isEmpty()){
                    break;
                }else{
                    writeCount++;
                }
            }
            payBW.flush();
            cancleBW.flush();
            queryBW.flush();

            payBW.close();
            cancleBW.close();
            queryBW.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
