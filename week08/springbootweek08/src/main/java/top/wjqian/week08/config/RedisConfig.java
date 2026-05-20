package top.wjqian.week08.config;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Redis配置类,配置RedisTemplate和自定义序列化器
 *
 * @author wjqian
 */
@Configuration
public class RedisConfig {

    /**
     * 配置 RedisTemplate
     * 使用 FastJson2 作为序列化器，提高性能并保证序列化的可读性
     *
     * @param factory Redis 连接工厂
     * @return 配置好的 RedisTemplate
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        // 设置key的序列化器为String序列化器，这样Redis中的key就是可读的字符串形式
        template.setKeySerializer(RedisSerializer.string());
        // 设置Hash类型的key序列化器为String序列化器
        template.setHashKeySerializer(RedisSerializer.string());
        // 创建FastJson2序列化器实例
        FastJsonRedisSerializer<Object> serializer = new FastJsonRedisSerializer<>(Object.class);
        // 设置value的序列化器为FastJson2序列化器，支持Java对象与JSON的高效转换
        template.setValueSerializer(serializer);
        // 设置Hash类型的value序列化器
        template.setHashValueSerializer(serializer);
        // 设置Redis连接工厂
        template.setConnectionFactory(factory);
        return template;
    }

    /**
     * FastJson2 Redis序列化器
     * 使用FastJson2进行对象的序列化和反序列化
     * 相比默认的JDK序列化器，具有更好的性能和可读性
     *
     * @param <T> 序列化的对象类型
     */
    record FastJsonRedisSerializer<T>(Class<T> clazz) implements RedisSerializer<T> {
        /**
         * 默认字符集：UTF-8
         */
        public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

        /**
         * 序列化：将Java对象转换为字节数组
         *
         * @param t 要序列化的对象
         * @return 序列化后的字节数组
         * @throws SerializationException 序列化异常
         */
        @Override
        public byte[] serialize(T t) throws SerializationException {
            if (null == t) {
                return new byte[0];
            }
            // 使用FastJson2将对象转为JSON字符串
            // WriteClassName特性会在JSON中包含类名信息，便于反序列化时确定类型
            return JSON.toJSONString(t, JSONWriter.Feature.WriteClassName).getBytes(DEFAULT_CHARSET);
        }

        /**
         * 反序列化：将字节数组转换为Java对象
         *
         * @param bytes 要反序列化的字节数组
         * @return 反序列化后的Java对象
         * @throws SerializationException 反序列化异常
         */
        @Override
        public T deserialize(byte[] bytes) throws SerializationException {
            if (null == bytes || bytes.length == 0) {
                return null;
            }
            // 将字节数组转换为字符串
            String str = new String(bytes, DEFAULT_CHARSET);
            // 使用FastJson2将JSON字符串转换为Java对象
            return JSON.parseObject(str, clazz);
        }
    }
}