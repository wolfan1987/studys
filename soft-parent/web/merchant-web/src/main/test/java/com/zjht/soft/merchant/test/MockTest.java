import com.zjht.soft.merchant.socket.WriterTestHexData;
import com.zjht.soft.merchant.util.OrderUtil;
import com.zjht.solar.commons.socket.utils.MessageLengthUtil;
import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Random;
import java.util.TimeZone;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by zjhtadmin on 2017/10/28.
 */
public class MockTest {

    private static final ExecutorService exec = Executors.newFixedThreadPool(100);


    public static void main(String[] args) {

        exec.submit(new WriterTestHexData());
        int tid = 44010618;
        //下单支付
        //        for (int i = 73861; i < 74161; i++) {
        for (int i = 0; i < 10000; i++) {
            tid++;
         /*   if (i % 5 == 0) {
                tid++;
            }*/

            if (i % 100 == 0) {
                tid = 44010618;
            }
            System.out.println("tid = " + tid);
            exec.submit(
               new RequestThread("172.16.94.31:8000", "", 1, 100000 + i, 100000 + i + 1, tid));
        }


        //查询订单
        //        for(int i = 1 ;i < 50; i ++){
        //            Executor.submit( new  RequestThread("172.16.94.31:8000",reqJson,1,i,i+1));
        //        }
    }
}


class RequestThread implements Runnable {


    private int reqCount = 1;
    private InputStream  in;
    private OutputStream out;
    private String payJson    = null;
    private String cancleJson = null;
    private String queryJson  = null;
    private String[] ipPorts;
    private Socket socket = null;
    private int startNum;
    private int endNum;
    private int tid;


    public RequestThread(
        String ipPort, String reqJson, int reqCount, int startNum, int endNum, int tid) {
        this.reqCount = reqCount;
        this.ipPorts = ipPort.split(":");
        this.payJson = reqJson;
        this.startNum = startNum;
        this.endNum = endNum;
        this.tid = tid;
    }

    @Override
    public void run() {
        for (int i = startNum; i < endNum; i++) {
            //            String newOrderId = getOrderId();
            String newOrderId = OrderUtil.getOrderId();

//            System.out.println("----newOrderId---2-- = " + newOrderId);
            this.payJson =
                "{\"txn_id\":\"841100\",\"txn_date\":\"20171205\",\"txn_time\":\"145632\",\"order_id\":\"" +
                newOrderId + "\",\"area_code\":\"GZ\",\"systrace\":\"" + i +
                "\",\"auth_code\":\"134594028006914772\",\"batch_no\":\"233890\",\"mid\":\"01\",\"tid\":\"" +
                tid + "\",\"txn_amt\":1}\n";
            /*this.cancleJson = "{\"mid\":\"01\",\"tid\":\"44010608\",\"systrace\":\"" + i +
                              "\",\"txn_id\":\"841200\",\"order_id\":\"" + newOrderId +
                              "\",\"batch_no\":\"233741\"}\n";
            this.queryJson = "{\"mid\":\"01\",\"tid\":\"44010608\",\"systrace\":\"" + i +
                             "\",\"txn_id\":\"841110\",\"order_id\":\"" + newOrderId +
                             "\",\"batch_no\":\"233741\"}\n";
*/
            try {
                socket = new Socket(ipPorts[0], Integer.parseInt(ipPorts[1]));
                in = socket.getInputStream();  //得到输入流（接收时用）
                out = socket.getOutputStream();

                byte[] jsonBytes = this.payJson.getBytes("UTF-8");
                byte[] sendDataLen = MessageLengthUtil.toAsicii(jsonBytes.length);
                byte[] payRequest = ArrayUtils.addAll(sendDataLen, jsonBytes);
                String payhex = printHexString(payRequest);
                WriterTestHexData.payHexQueue.add(payhex);
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
                long startTime = System.currentTimeMillis();
                //out.write(("0"+xmlBytes.length).getBytes());
                if (startTime > 0) {
                    return;
                }
                out.write(payRequest);
                out.flush();
                //  System.out.println("发送的数据是："+new String(xmlBytes,"UTF-8").toString());
                //   System.out.println("jsonBytes.lengh="+xmlBytes.length);
                int totalBytesRcvd = 0;  //接收到了总字节大小
                byte[] result = new byte[1024];
                byte[] dataLen = new byte[4];
                int len = -1;

                while ((totalBytesRcvd = in.read(result)) > 0) {
                    if (len < 0) {
                        System.arraycopy(result, 0, dataLen, 0, 4);
                        len = Integer.parseInt(new String(dataLen, "GBK"));
                        if (len == (totalBytesRcvd - 4)) {
                            break;
                        }
                    }
                }
                long endTime = System.currentTimeMillis();
                if ((totalBytesRcvd - 4) == len) {
                    String jsonResult = new String(result, "UTF-8");
                    System.out.println(
                        "startTime=" + getHMS(startTime) + ", endTime=" + getHMS(endTime) +
                        ",  costTime=" + ((endTime - startTime) / 1000) + ",orderId=" + newOrderId);
                    System.out.println("从商家平台返回的数据是: " + jsonResult);
                    //  System.out.println(""+startTime+","+endTime+","+((endTime-startTime)/1000)+","+newOrderId);
                    //  System.out.println(((endTime-startTime))+",orderId="+newOrderId);
                } else {
                    //   System.out.println("从商家平台返回数据异常！totalBytesRcvd="+totalBytesRcvd+"dataLen="+len);
                }
                socket.close();


            } catch (UnknownHostException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            } catch (Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private String getHMS(long ms) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        String hms = formatter.format(ms);
        return hms;
    }

    private String getOrderId() {
        Random orderIdRandom = new Random();
        int orderNum = 0;
        String orderId = null;
        while (true) {
            orderNum = Math.abs(orderIdRandom.nextInt(999));
            if (orderNum >= 100) {
                orderId = "711" + orderNum + System.currentTimeMillis();
                break;
            } else if (orderNum <= 100) {
                orderId = "7110" + orderNum + System.currentTimeMillis();
                break;
            } else {
                continue;
            }
        }
        return orderId;
    }

    public static String printHexString(byte[] b) {
        StringBuffer temp = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            temp.append(hex);
        }
        //        System.out.println(temp);
        return temp.toString() + "\n";
    }

}


