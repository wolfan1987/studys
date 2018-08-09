package com.zjht.soft.merchant.socket.component;

import com.alibaba.fastjson.JSON;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelId;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 存放Channel的容器
 * <br>
 * Created by 黄灿贤 on 2017年9月21日
 *
 * @version 1.0-SNAPSHOT
 */
public class CustomChannelGroup extends DefaultChannelGroup {

    private static final Logger log = LoggerFactory.getLogger(CustomChannelGroup.class);

    // 保存业务ID[唯一的] 与 ChannelID的关联映射
    private static Map<String, ChannelId> BUSINESS_MAP = new ConcurrentHashMap<>();
    // Channel关闭，更新BUSINESS_MAP
    private final  ChannelFutureListener  remover      = new ChannelFutureListener() {
        @Override
        public void operationComplete(ChannelFuture future) throws Exception {
           log.debug("Channel关闭前，更新BUSINESS_MAP。");
            CustomChannelGroup.this.removeChannelId(future.channel());
        }
    };

    /**
     * @param groupName
     */
    public CustomChannelGroup(String groupName) {
        super(groupName, GlobalEventExecutor.INSTANCE);
    }

    /**
     * @param groupName
     * @param executor
     */
    public CustomChannelGroup(String groupName, EventExecutor executor) {
        super(groupName, executor);
    }

    // /////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 获取活动的Channel内存对象。
     *
     * @param businessId 唯一key值
     * @return Channel内存对象
     */
    public Channel get(String businessId) {
        ChannelId id = BUSINESS_MAP.get(businessId);
        if (id == null) {
            log.debug("businessId={}，ChannelId不存在BUSINESS_MAP里", businessId);
            return null;
        }
       log.info("从容器获取channe，businessId={}", businessId);
        Channel channel = super.find(id);
        if (channel != null) {
          log.info("获取到channel，isActive={}，channelId={}", channel.isActive(), id.asLongText());
            return channel;
        } else {
           log.debug("businessId={}，Channel不存在Group里", businessId);
            return null;
        }
    }

    /**
     * 将Channel放入内存中，如果已经存在，则将原Channel关闭，并覆盖。
     *
     * @param businessId 唯一key值
     * @param channel    channel对象
     * @return
     */
    public void add(String businessId, Channel channel) {
       log.debug("channel添加到容器:{}", businessId);
        Channel oldChannel = this.get(businessId);
        // 覆盖原来的channel时需要将原channel关闭
        if (oldChannel != null && oldChannel.compareTo(channel) != 0) {
            log.warn("旧的Channel还存在，先关闭。channelId={}", oldChannel.id().asLongText());
            oldChannel.close();
        }
        boolean added = super.add(channel);
        if (added) {
            BUSINESS_MAP.put(businessId, channel.id());
            channel.closeFuture().addListener(remover);
           log.info("channel添加到容器成功。channelId={}", channel.id().asLongText());
        }
    }

    /**
     * 移除BUSINESS_MAP的ChannelId，注意: 关闭Channel时，自动会调用本方法移除ChannelId内存对象。
     *
     * @param channel 对象
     */
    private void removeChannelId(Channel channel) {
        log.debug("channeId={} , isActive={}", channel.id().asLongText(), channel.isActive());
        // 获得ChannelId对应的businessId
        String businessId = null;
        ChannelId channelId = channel.id();
        for (Map.Entry<String, ChannelId> entry : BUSINESS_MAP.entrySet()) {
            if (entry.getValue().compareTo(channelId) == 0) {
                businessId = entry.getKey();
                break;
            }
        }
        // 移除ChannelId
        if (businessId != null) {
            log.debug("准备把ChannelId从容器【BUSINESS_MAP】移除，businessId:{}", businessId);
            BUSINESS_MAP.remove(businessId);
            log.debug("成功把ChannelId从容器【BUSINESS_MAP】移除，businessId={}", businessId);
        }
        channel.closeFuture().removeListener(remover);
    }

    /**
     * 推送String数据到Client端
     *
     * @param businessId       唯一key值
     * @param msg              需要推送消息
     * @param needCloseChannel 是否关闭通道
     * @return 是否已经推送
     */
    public boolean push(String businessId, Object msg, boolean needCloseChannel) {
        log.info("businessId={}, msg={}", businessId, msg);
        Channel channel = this.get(businessId);
        if (channel != null) {
            String text;
            if (msg instanceof String) {
                text = (String) msg;
            } else {
                text = JSON.toJSONString(msg);
            }
           log.info("customchannelGroup返回订单查询结果SO-->{}", text);
            channel.writeAndFlush(text);
            if (needCloseChannel) {
                channel.close();
            }
            return true;
        }
        return false;
    }

}
