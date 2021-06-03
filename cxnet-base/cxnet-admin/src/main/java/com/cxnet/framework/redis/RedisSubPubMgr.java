package com.cxnet.framework.redis;

import com.cxnet.common.utils.Threads;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * redis订阅发布管理
 */
@Slf4j
@Component
public class RedisSubPubMgr implements ApplicationRunner {

    private static JedisPool jedisPool = new JedisPool(new JedisPoolConfig(), RedisConfig.host, RedisConfig.port, RedisConfig.timeout, RedisConfig.password);

    /**
     * 发布消息 data通道
     */
    public static void publishDataMsg(String message) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            if (jedis != null) {
                jedis.publish(RedisConfig.dataChannel, message);
                jedis.close();
            } else {
                log.error("Redis connection err.");
            }
        } catch (Exception e) {
            log.error("Redis connection err." + e.toString());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 订阅线程
     */
    @Override
    @Async
    public void run(ApplicationArguments args) throws Exception {
        while (true) {
            try {
                Jedis jedis = jedisPool.getResource();
                jedis.subscribe(new RedisSubscriber(), RedisConfig.dataChannel);
            } catch (Exception e) {
                Threads.sleep(5000);
            }
        }
    }
}
