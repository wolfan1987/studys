package com.zjht.soft.merchant.socket.component;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zjht.soft.merchant.entity.MerchantTrans;
import com.zjht.soft.merchant.service.MerchantTransService;
import com.zjht.soft.merchant.socket.business.BizInterface;
import com.zjht.soft.merchant.socket.business.BizTypeEnum;
import com.zjht.soft.merchant.socket.business.OrderContextBiz;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Date;

@Sharable
@Component
public class CustomServerHandler extends SimpleChannelInboundHandler<String>
    implements ApplicationContextAware {

    private static final Logger log = LoggerFactory.getLogger(CustomServerHandler.class);


    public static final AttributeKey<String> BUSINESS_ID_KEY = AttributeKey.valueOf(
        BizInterface.BUSINESS_ID_FIELD);

    private static ApplicationContext SpringContext = null;


    @Reference(version = "1.0.0")
    private MerchantTransService transService;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContext = applicationContext;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.debug("与客户端建立链接:{}", ctx.channel().remoteAddress().toString());
        super.channelActive(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext context, String requestJson)
        throws Exception {

        OrderContextBiz orderContextBiz = SpringContext.getBean(OrderContextBiz.class);
        orderContextBiz.produceOrder(context, requestJson);

        //        log.debug("NettyServer receive requestJson={}", requestJson);
        //        Channel channel = context.channel();
        //        JSONObject clientData = JSON.parseObject(requestJson);// 解释Json
        //        // 必要的基础参数检验
        //        Map<String, Object> result = this.checkData(clientData);
        //        boolean success = MapUtils.getBooleanValue(result, "success");
        //        if (!success) {
        //            log.warn("基础参数检验失败===>{}", MapUtils.getString(result, "message"));
        //            String resposeJson = JSON.toJSONString(result);
        //            channel.writeAndFlush(resposeJson);
        //            channel.close();
        //        } else {
        //            // 异常撤单时要用
        //            String businessId = clientData.getString(BizInterface.BUSINESS_ID_FIELD);
        //            Attribute<String> attr = channel.attr(BUSINESS_ID_KEY);
        //            attr.set(businessId);
        //            log.debug("channel.attr值为:{}", attr.get());
        //            // 保存channel
        //            CustomNettyServer.ChannelGroup.add(businessId, channel);
        //            context.fireChannelReadComplete();
        //            // 得到业务处理类
        //         //   Map<String, BizInterface> beanMap = SpringContext.getBeansOfType(BizInterface.class, false, true);
        //            OrderBiz  orderBiz = SpringContext.getBean(OrderBiz.class);
        //            //orderBiz.doTask(channel, requestJson);
        //            orderBiz.doNewTask(context, requestJson);
        ////            for (Entry<String, BizInterface> entry : beanMap.entrySet()) {
        ////                BizInterface bean = entry.getValue();
        ////                Executor.submit(() -> bean.doTask(channel, requestJson));
        ////            }
        //        }
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Attribute<String> attr = ctx.channel().attr(BUSINESS_ID_KEY);
        if (attr.get() != null) {
            String businessId = attr.get();
            log.info(
                "businessId={}的记录状态改成待撤消[-2]，原因是:{}", businessId, ExceptionUtils.getMessage(cause));
            MerchantTrans data = transService.findByOrderId(businessId, BizTypeEnum.PAY.getValue());
            if (data != null) {
                data.setOrderStatus(-2);
                data.setUpdateTime(new Date());
                transService.update(data);
            } else {
                log.warn("查找不到businessId={}的记录", businessId);
            }
        } else {
            super.exceptionCaught(ctx, cause);
        }
    }

}

