import com.alibaba.fastjson.JSON;
import com.zjht.soft.bluelotus.socket.entity.PayOrderReq;
import com.zjht.soft.merchant.util.OrderUtil;
import com.zjht.solar.commons.socket.netty.codec.MessageDecoder;
import com.zjht.solar.commons.socket.netty.codec.MessageEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.net.InetSocketAddress;

/**
 * Created by leaves chen[leaves615@gmail.com] on 2017/4/19.
 *
 * @Author leaves chen[leaves615@gmail.com]
 */
public class Client {
    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group).channel(NioSocketChannel.class).handler(
            new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel channel) throws Exception {
                    ChannelPipeline pipeline = channel.pipeline();
                    pipeline.addLast(new LoggingHandler(LogLevel.INFO));
                    pipeline.addLast(new MessageDecoder());
                    pipeline.addLast(new MessageEncoder());
                    pipeline.addLast(new CustomHandler());
                }
            });

        //        ChannelFuture channelFuture = bootstrap.connect(
        //            new InetSocketAddress("172.16.100.125", 4363)).sync();
        ChannelFuture channelFuture = bootstrap.connect(
            new InetSocketAddress("127.0.0.1", 8000)).sync();
        Channel channel = channelFuture.channel();
        while (!channel.isWritable()) {
            Thread.currentThread().sleep(1000);
        }
        //        channel.writeAndFlush("GET / HTTP/1.1");
        System.out.println("sended");
        //        Thread.sleep(30000000);
    }

    static class CustomHandler extends SimpleChannelInboundHandler<String> {
        int times = 0;

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            String orderId = OrderUtil.getOrderId();

//            String orderId = "201711061019264010169303";

            PayOrderReq orderReq = new PayOrderReq();

            orderReq.setMid("01");
            orderReq.setTxnId("841100");
            orderReq.setTid("44010608");
            orderReq.setAuthCode("6224191201612359161");
            orderReq.setTxnAmt(1);
            orderReq.setOrderId(orderId);
            ctx.write(JSON.toJSONString(orderReq));
            ctx.flush();
        }


        @Override
        protected void channelRead0(
            ChannelHandlerContext channelHandlerContext, String s) throws Exception {
            System.out.print("recive:");
            System.out.println(s);
            if (times < 10) {
                times++;
                channelHandlerContext.write("ss" + times);
                channelHandlerContext.flush();
            }
            channelHandlerContext.fireChannelReadComplete();
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
        }
    }

}
