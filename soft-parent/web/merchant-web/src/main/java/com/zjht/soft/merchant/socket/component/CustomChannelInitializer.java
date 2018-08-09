package com.zjht.soft.merchant.socket.component;

import com.zjht.solar.commons.socket.netty.codec.MessageDecoder;
import com.zjht.solar.commons.socket.netty.codec.MessageEncoder;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Sharable
@Component
public class CustomChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Autowired
    private CustomServerHandler myServerHandler;

    public void initChannel(SocketChannel channel) {
        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addLast(new LoggingHandler(LogLevel.INFO));
        pipeline.addLast(new MessageEncoder());
        pipeline.addLast(new MessageDecoder());
        pipeline.addLast(myServerHandler);
    }

}
