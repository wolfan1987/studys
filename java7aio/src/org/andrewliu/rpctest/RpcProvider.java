package org.andrewliu.rpctest;
/**
 * @Author AndrewLiu (liudaan@chinaexpresscard.com)
 * @Description:
 * @Date: Created in 2018/6/8 15:38
 * @Modifyed By:
 * @Other: A Lucky Man
 */
public class RpcProvider {
    public static void main(String[] args) throws Exception {
        HelloService service = new HelloServiceImpl();
        SimpleRpcFramework.export(service, 1234);
    }
}
