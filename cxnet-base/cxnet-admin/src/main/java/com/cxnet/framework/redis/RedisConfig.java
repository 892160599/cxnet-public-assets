package com.cxnet.framework.redis;

import com.cxnet.framework.config.FastJson2JsonRedisSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * redis配置
 *
 * @author cxnet
 */
@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {

    public static String host;
    public static int port;
    public static int timeout;
    public static String password;
    public static String dataChannel;

    @Value("${spring.redis.host}")
    public void setHost(String host) {
        RedisConfig.host = host;
    }

    @Value("${spring.redis.port}")
    public void setPort(int port) {
        RedisConfig.port = port;
    }

    @Value("${spring.redis.timeout}")
    public void setTimeout(int timeout) {
        RedisConfig.timeout = timeout;
    }

    @Value("${spring.redis.requirePass}")
    public void setPassword(String password) {
        RedisConfig.password = password;
    }

    @Value("${spring.redis.dataChannel}")
    public void setDataChannel(String dataChannel) {
        RedisConfig.dataChannel = dataChannel;
    }

    @Bean
    @SuppressWarnings(value = {"unchecked", "rawtypes"})
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        FastJson2JsonRedisSerializer serializer = new FastJson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        serializer.setObjectMapper(om);
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        // key采用String的序列化方式
        template.setKeySerializer(stringRedisSerializer);
        // hash的key也采用String的序列化方式
        template.setHashKeySerializer(stringRedisSerializer);
        // value序列化方式采用FastJson
        template.setValueSerializer(serializer);
        // hash的value序列化方式采用FastJson
        template.setHashValueSerializer(serializer);
        template.afterPropertiesSet();
        return template;
    }
}
