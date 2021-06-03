package com.cxnet.framework.redis;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.JedisPubSub;

/**
 * redis订阅
 */
@Slf4j
public class RedisSubscriber extends JedisPubSub {

    /**
     * 收到消息 会调用
     */
    @Override
    public void onMessage(String channel, String message) {
        log.debug(String.format("receive redis published message, channel %s, message %s", channel, message));
    }

    /**
     * 订阅了频道 会调用
     */
    @Override
    public void onSubscribe(String channel, int subscribedChannels) {
        log.debug(String.format("subscribe redis channel success, channel %s, subscribedChannels %d",
                channel, subscribedChannels));
    }


    /**
     * 取消订阅 会调用
     */
    @Override
    public void onUnsubscribe(String channel, int subscribedChannels) {
        log.debug(String.format("unsubscribe redis channel, channel %s, subscribedChannels %d",
                channel, subscribedChannels));
    }
}
