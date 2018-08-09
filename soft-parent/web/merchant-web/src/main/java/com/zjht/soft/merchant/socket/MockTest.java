package com.zjht.soft.merchant.socket;

import com.zjht.soft.bluelotus.socket.entity.PayOrderReq;
import com.zjht.soft.bluelotus.socket.entity.TransactionQueryReq;
import com.zjht.soft.bluelotus.socket.vo.PayOrderReqVO;
import com.zjht.soft.merchant.entity.MerchantTrans;
import com.zjht.soft.merchant.socket.polling.GetRevokeDataJob;
import com.zjht.solar.commons.socket.utils.MessageLengthUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by zjhtadmin on 2017/10/28.
 */
public class MockTest {

    private static final ExecutorService exec = Executors.newFixedThreadPool(100);


    public static void main(String[] args){



        //  exec.submit(new WriterTestHexData());
        //下单支付
            for(int k = 0 ; k<1 ; k++){
                int startTerm = 44010608;
                for(int i = 0 ;i<1; i ++){
                    // exec.submit( new  RequestThread(startTerm,"172.16.94.31:8000","",1,100000+i,100000+i+1));
                    //exec.submit( new  RequestThread(startTerm,"172.16.94.104:6008","",1,100000+i,100000+i+1));
                    exec.submit( new  RequestThread(startTerm,"172.16.89.164:8000","",1,100000+i,100000+i+1));
                    ++startTerm;
                }
            }


        //查询订单
//        for(int i = 1 ;i < 50; i ++){
//            Executor.submit( new  RequestThread("172.16.94.31:8000",reqJson,1,i,i+1));
//        }
    }
}


class  RequestThread  implements  Runnable{


    private  int   reqCount=1;
    private InputStream in ;
    private OutputStream out;
    private  String   payJson  = null;
    private  String   cancleJson = null;
    private  String   queryJson = null;
    private   String[] ipPorts;
    private Socket socket = null;
    private int  startNum;
    private  int  endNum;
    private  int  startTerm;
    public   RequestThread(int startTerm,String  ipPort,String  reqJson,int reqCount,int startNum,int endNum){
        this.startTerm = startTerm;
        this.reqCount = reqCount;
        this.ipPorts = ipPort.split(":");
        this.payJson = reqJson;
        this.startNum = startNum;
        this.endNum = endNum;
    }

    @Override
    public  void run(){
        for(int i = startNum ; i < endNum;i++){
            String  newOrderId = getOrderId();

           this.payJson =  "{\"txn_id\":\"841100\",\"txn_date\":\"20171204\",\"txn_time\":\"145532\",\"order_id\":\""+newOrderId+"\",\"area_code\":\"GZ\",\"systrace\":\""+i+"\",\"auth_code\":\"6266187582589275952\",\"batch_no\":\"233748\",\"mid\":\"01\",\"tid\":\""+startTerm+"\",\"txn_amt\":1}\n";
            this.cancleJson = "{\"batch_no\":\"000000\",\"mid\":\"01\",\"order_id\":\"201711071254413580751303\",\"systrace\":\"A00024\",\"tid\":\"44010617\",\"txn_amt\":1,\"txn_id\":\"841200\"}\n";
            this.queryJson =  "{\"mid\":\"01\",\"tid\":\"44010608\",\"systrace\":\""+i+"\",\"txn_id\":\"841110\",\"order_id\":\""+newOrderId+"\",\"batch_no\":\"233741\"}\n";

            try {
                socket= new Socket(ipPorts[0],Integer.parseInt(ipPorts[1]));
                in = socket.getInputStream();  //得到输入流（接收时用）
                out = socket.getOutputStream();

                byte[]  jsonBytes = this.payJson.getBytes("UTF-8");
                byte[]  sendDataLen = MessageLengthUtil.toAsicii(jsonBytes.length);
                byte[] payRequest = ArrayUtils.addAll(sendDataLen,jsonBytes);
              //   String  payhex =  printHexString(payRequest);
             //   WriterTestHexData.payHexQueue.add(payhex);
//                  jsonBytes = this.cancleJson.getBytes("UTF-8");
//                  sendDataLen = MessageLengthUtil.toAsicii(jsonBytes.length);
//                 payRequest = ArrayUtils.addAll(sendDataLen,jsonBytes);
//                String  cancleHex = printHexString(payRequest);
//                WriterTestHexData.cancleHexQueue.add(cancleHex);
//                jsonBytes = this.queryJson.getBytes("UTF-8");
//                sendDataLen = MessageLengthUtil.toAsicii(jsonBytes.length);
//                payRequest = ArrayUtils.addAll(sendDataLen,jsonBytes);
//                String  queryHex = printHexString(payRequest);
//                WriterTestHexData.queryHexQueue.add(queryHex);
                long  startTime =System.currentTimeMillis();
                //out.write(("0"+xmlBytes.length).getBytes());
//                if(startTime>0){
//                    return;
//                }
                out.write(payRequest);
                out.flush();
                System.out.println("发送的数据是："+new String(payRequest,"UTF-8").toString());
             //   System.out.println("jsonBytes.lengh="+xmlBytes.length);
                int totalBytesRcvd = 0;  //接收到了总字节大小
                byte[]  result = new byte[1024];
                byte[]  dataLen = new byte[4];
                int len = -1;

                while((totalBytesRcvd = in.read(result))>0){
                    if(len<0){
                        System.arraycopy(result, 0, dataLen, 0, 4);
                        len = Integer.parseInt(new String(dataLen,"GBK"));
                        if(len==(totalBytesRcvd-4)){
                            break;
                        }
                    }
                }
                long  endTime =System.currentTimeMillis();
                if((totalBytesRcvd-4)==len){
                    String  jsonResult = new String(result,"UTF-8");
                    long  lenTime = endTime-startTime;
                   System.out.println("startTime="+getHMS(startTime)+", endTime="+getHMS(endTime)+",  costTimes="+lenTime+",costTime="+(lenTime/1000)+",orderId="+newOrderId);
                   System.out.println("从商家平台返回的数据是: "+ jsonResult);
                  //  System.out.println(""+startTime+","+endTime+","+((endTime-startTime)/1000)+","+newOrderId);
                  //  System.out.println(((endTime-startTime))+",orderId="+newOrderId);
                }else{
                 //   System.out.println("从商家平台返回数据异常！totalBytesRcvd="+totalBytesRcvd+"dataLen="+len);
                }
                socket.close();


            } catch (UnknownHostException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            } catch(Exception e){
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private  String   getHMS(long  ms){
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        String hms = formatter.format(ms);
        return  hms;
    }

    private  String   getOrderId(){
        Random orderIdRandom = new Random();
        int orderNum = 0;
        String orderId = null;
        while (true) {
            orderNum = Math.abs(orderIdRandom.nextInt(999));
            if (orderNum >= 100) {
                orderId = "711" + orderNum+System.currentTimeMillis();
                break;
            } else if (orderNum  <= 100) {
                orderId="7110" + orderNum+System.currentTimeMillis();
                break;
            } else {
                continue;
            }
        }
        return orderId;
    }

    public static String  printHexString( byte[] b) {
        StringBuffer  temp =new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            temp.append(hex);
        }
        System.out.println(temp);
        return temp.toString()+"\n";
    }

}


