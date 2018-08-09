package org.andrewliu.rpctest;
/**
 * @Author AndrewLiu (liudaan@chinaexpresscard.com)
 * @Description:
 * @Date: Created in 2018/6/8 15:39
 * @Modifyed By:
 * @Other: A Lucky Man
 */
public class RpcConsumer {
    public static void main(String[] args) throws Exception {
        HelloService service = SimpleRpcFramework.refer(HelloService.class, "127.0.0.1", 1234);
        for (int i = 0; i < 500; i ++) {
            String hello = service.hello("World" + i);
            System.out.println(hello);
            Thread.sleep(1000);
        }
    }
}
