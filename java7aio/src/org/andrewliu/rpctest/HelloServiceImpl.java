package org.andrewliu.rpctest;

/**
 * @Author AndrewLiu (liudaan@chinaexpresscard.com)
 * @Description:
 * @Date: Created in 2018/6/8 15:46
 * @Modifyed By:
 * @Other: A Lucky Man
 */
public class HelloServiceImpl  implements  HelloService{

    public String hello(String name) {
        return "Hello " + name;
    }
}
