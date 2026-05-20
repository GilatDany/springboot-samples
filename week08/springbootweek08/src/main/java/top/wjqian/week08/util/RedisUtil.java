package top.wjqian.week08.util;

import com.alibaba.fastjson2.JSON;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 基于 RedisTemplate<String, Object> 提供了便捷的常用 Redis 操作方法
 * <p>
 * 主要特性
 * 序列化支持
 * 配合 RedisConfig 中的 FastJson2 序列化器
 * 支持 Java 对象与 JSON 的高效转换
 * 提供泛型方法自动处理类型转换
 * <p>
 * 五大数据类型操作
 * String（字符串）：set/get/increment/decrement/expire 等
 * Hash（哈希）：hSet/hGet/hGetAll/hDelete 等
 * List（列表）：lLeftPush/lRightPush/lRange/lPop 等
 * Set（集合）：sAdd/sMembers/sIsMember/sRemove 等
 * ZSet（有序集合）：zAdd/zRange/zRevRange/zScore 等
 * 通用操作
 * key 管理：删除、过期时间设置、存在性判断、模式匹配查询
 * 批量操作：批量删除、批量读取
 * <p>
 * 设计亮点
 * 智能类型转换：泛型方法先检查类型是否匹配，不匹配则通过 JSON 二次转换
 * 空值安全：所有方法都有 null 检查和边界处理
 * Component 注解：可直接注入到其他服务中使用
 * Lombok 简化：使用 @RequiredArgsConstructor 自动生成构造函数
 * <p>
 * 适用场景
 * 验证码存储、用户会话管理、排行榜、缓存、消息队列、购物车等各类 Redis 应用场景。
 * <p>
 * 文档：{@code docs/RedisUtil使用场景与说明.md}；短信验证码落地见 {@code docs/Redis场景设计-短信验证码.md}（{@code top.mqxu.week08.sms}）。
 *
 * @author mqxu
 */
@Getter
@Component
@RequiredArgsConstructor
public class RedisUtil {

    private final RedisTemplate<String, Object> redisTemplate;


    /**
     * 判断 key 是否存在
     *
     * @param key 键
     * @return true/false
     */
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 删除 key
     *
     * @param key 键
     */
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 批量删除 key
     *
     * @param keys 键
     */
    public void delete(String... keys) {
        if (keys == null || keys.length == 0) {
            return;
        }
        redisTemplate.delete(List.of(keys));
    }

    /**
     * 批量删除 key
     *
     * @param keys 键
     */
    public void delete(Collection<String> keys) {
        if (keys == null || keys.isEmpty()) {
            return;
        }
        redisTemplate.delete(keys);
    }


    /**
     * 获取 key 列表
     *
     * @param pattern 匹配模式
     * @return 匹配的 key 列表
     */
    public Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    /**
     * 设置 key 过期时间
     *
     * @param key     键
     * @param timeout 过期时间
     * @param unit    时间单位
     * @return true/false
     */
    public Boolean expire(String key, long timeout, TimeUnit unit) {
        return redisTemplate.expire(key, timeout, unit);
    }

    /**
     * 获取 key 过期时间
     *
     * @param key 键
     * @return 过期时间
     */
    public Long getExpire(String key) {
        return redisTemplate.getExpire(key);
    }

    /**
     * 获取 key 过期时间
     *
     * @param key  键
     * @param unit 时间单位
     * @return 过期时间
     */
    public Long getExpire(String key, TimeUnit unit) {
        return redisTemplate.getExpire(key, unit);
    }


    /**
     * 写入 key
     *
     * @param key   键
     * @param value 值
     */
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 写入 key
     *
     * @param key     键
     * @param value   值
     * @param timeout 过期时间
     * @param unit    时间单位
     */
    public void set(String key, Object value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }


    /**
     * 写入 key，如果 key 不存在则写入
     *
     * @param key   键
     * @param value 值
     * @return true/false
     */
    public Boolean setIfAbsent(String key, Object value) {
        return Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(key, value));
    }

    /**
     * 写入 key，如果 key 不存在则写入
     *
     * @param key     键
     * @param value   值
     * @param timeout 过期时间
     * @param unit    时间单位
     * @return true/false
     */
    public Boolean setIfAbsent(String key, Object value, long timeout, TimeUnit unit) {
        return Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(key, value, timeout, unit));
    }

    /**
     * 读取 key
     *
     * @param key 键
     * @return 值
     */
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }


    /**
     * 读取 key
     *
     * @param key   键
     * @param clazz 值类型
     * @return 值
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> clazz) {
        Object raw = redisTemplate.opsForValue().get(key);
        if (raw == null) {
            return null;
        }
        if (clazz.isInstance(raw)) {
            return (T) raw;
        }
        return JSON.parseObject(JSON.toJSONString(raw), clazz);
    }

    /**
     * 批量读取 key
     *
     * @param keys 键
     * @return 值
     */
    public List<Object> multiGet(Collection<String> keys) {
        if (keys == null || keys.isEmpty()) {
            return List.of();
        }
        return redisTemplate.opsForValue().multiGet(keys);
    }

    /**
     * 获取并设置 key
     *
     * @param key   键
     * @param value 值
     * @return 原值
     */
    public Object getAndSet(String key, Object value) {
        return redisTemplate.opsForValue().getAndSet(key, value);
    }


    /**
     * 自增
     *
     * @param key 键
     * @return 自增后的值
     */
    public Long increment(String key) {
        return redisTemplate.opsForValue().increment(key);
    }

    /**
     * 自增
     *
     * @param key   键
     * @param delta 增量
     * @return 自增后的值
     */
    public Long increment(String key, long delta) {
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 减少 key
     *
     * @param key 键
     * @return 减少后的值
     */
    public Long decrement(String key) {
        return redisTemplate.opsForValue().decrement(key);
    }

    /**
     * 减少 key
     *
     * @param key   键
     * @param delta 减少量
     * @return 减少后的值
     */
    public Long decrement(String key, long delta) {
        return redisTemplate.opsForValue().decrement(key, delta);
    }


    /**
     * 写入 key
     *
     * @param key   键
     * @param field 字段
     * @param value 值
     */
    public void hSet(String key, String field, Object value) {
        redisTemplate.opsForHash().put(key, field, value);
    }

    /**
     * 写入 key
     *
     * @param key 键
     * @param map 键值对
     */
    public void hSetAll(String key, Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return;
        }
        redisTemplate.opsForHash().putAll(key, map);
    }

    /**
     * 读取 key
     *
     * @param key   键
     * @param field 字段
     * @return 值
     */
    public Object hGet(String key, String field) {
        return redisTemplate.opsForHash().get(key, field);
    }


    /**
     * 读取 key
     *
     * @param key   键
     * @param field 字段
     * @param clazz 值类型
     * @return 值
     */
    @SuppressWarnings("unchecked")
    public <T> T hGet(String key, String field, Class<T> clazz) {
        Object raw = redisTemplate.opsForHash().get(key, field);
        if (raw == null) {
            return null;
        }
        if (clazz.isInstance(raw)) {
            return (T) raw;
        }
        return JSON.parseObject(JSON.toJSONString(raw), clazz);
    }

    /**
     * 批量读取 key
     *
     * @param key 键
     * @return 值
     */
    public Map<Object, Object> hGetAll(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 删除 key
     *
     * @param key    键
     * @param fields 字段
     */
    public void hDelete(String key, String... fields) {
        if (fields == null || fields.length == 0) {
            return;
        }
        redisTemplate.opsForHash().delete(key, (Object[]) fields);
    }

    /**
     * 判断 key 是否存在
     *
     * @param key   键
     * @param field 字段
     * @return true/false
     */
    public Boolean hHasKey(String key, String field) {
        return redisTemplate.opsForHash().hasKey(key, field);
    }


    /**
     * 写入 key
     *
     * @param key   键
     * @param value 值
     * @return 索引
     */
    public Long lLeftPush(String key, Object value) {
        return redisTemplate.opsForList().leftPush(key, value);
    }

    /**
     * 批量写入 key
     *
     * @param key    键
     * @param values 值
     * @return 索引
     */
    public Long lLeftPushAll(String key, Object... values) {
        if (values == null || values.length == 0) {
            return 0L;
        }
        return redisTemplate.opsForList().leftPushAll(key, values);
    }

    /**
     * 写入 key
     *
     * @param key   键
     * @param value 值
     * @return 索引
     */
    public Long lRightPush(String key, Object value) {
        return redisTemplate.opsForList().rightPush(key, value);
    }

    /**
     * 批量写入 key
     *
     * @param key    键
     * @param values 值
     * @return 索引
     */
    public Long lRightPushAll(String key, Object... values) {
        if (values == null || values.length == 0) {
            return 0L;
        }
        return redisTemplate.opsForList().rightPushAll(key, values);
    }

    /**
     * 读取 key
     *
     * @param key   键
     * @param start 开始索引
     * @param end   结束索引
     * @return 值
     */
    public List<Object> lRange(String key, long start, long end) {
        return redisTemplate.opsForList().range(key, start, end);
    }

    /**
     * 获取 key 的长度
     *
     * @param key 键
     * @return 长度
     */
    public Long lSize(String key) {
        return redisTemplate.opsForList().size(key);
    }

    /**
     * 读取 key
     *
     * @param key 键
     * @return 值
     */
    public Object lLeftPop(String key) {
        return redisTemplate.opsForList().leftPop(key);
    }


    /**
     * 读取 key
     *
     * @param key 键
     * @return 值
     */
    public Object lRightPop(String key) {
        return redisTemplate.opsForList().rightPop(key);
    }

    /**
     * 批量写入 key
     *
     * @param key     键
     * @param members 成员
     * @return 添加的个数
     */
    public Long sAdd(String key, Object... members) {
        if (members == null || members.length == 0) {
            return 0L;
        }
        return redisTemplate.opsForSet().add(key, members);
    }

    /**
     * 读取 key
     *
     * @param key 键
     * @return 值
     */
    public Set<Object> sMembers(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * 判断 key 是否包含 member
     *
     * @param key    键
     * @param member 成员
     * @return true/false
     */
    public Boolean sIsMember(String key, Object member) {
        return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(key, member));
    }

    /**
     * 删除 key
     *
     * @param key     键
     * @param members 删除的成员
     * @return 删除的个数
     */
    public Long sRemove(String key, Object... members) {
        if (members == null || members.length == 0) {
            return 0L;
        }
        return redisTemplate.opsForSet().remove(key, members);
    }

    /**
     * 获取 key 的长度
     *
     * @param key 键
     * @return 长度
     */
    public Long sSize(String key) {
        return redisTemplate.opsForSet().size(key);
    }


    /**
     * 写入 key
     *
     * @param key   键
     * @param value 值
     * @param score 分数
     * @return true/false
     */
    public Boolean zAdd(String key, Object value, double score) {
        return Boolean.TRUE.equals(redisTemplate.opsForZSet().add(key, value, score));
    }

    /**
     * 批量写入 key
     *
     * @param key    键
     * @param tuples 键值对
     * @return 添加的个数
     */
    public Long zAdd(String key, Set<ZSetOperations.TypedTuple<Object>> tuples) {
        if (tuples == null || tuples.isEmpty()) {
            return 0L;
        }
        return redisTemplate.opsForZSet().add(key, tuples);
    }

    /**
     * 读取 key
     *
     * @param key   键
     * @param start 开始索引
     * @param end   结束索引
     * @return 值
     */
    public Set<Object> zRange(String key, long start, long end) {
        return redisTemplate.opsForZSet().range(key, start, end);
    }

    /**
     * 读取 key
     *
     * @param key   键
     * @param start 开始索引
     * @param end   结束索引
     * @return 值
     */
    public Set<ZSetOperations.TypedTuple<Object>> zRangeWithScores(String key, long start, long end) {
        return redisTemplate.opsForZSet().rangeWithScores(key, start, end);
    }

    /**
     * 按 score 从大到小取成员（排行榜常用）
     */
    public Set<Object> zRevRange(String key, long start, long end) {
        return redisTemplate.opsForZSet().reverseRange(key, start, end);
    }

    /**
     * 按 score 从大到小取成员及分数
     */
    public Set<ZSetOperations.TypedTuple<Object>> zRevRangeWithScores(String key, long start, long end) {
        return redisTemplate.opsForZSet().reverseRangeWithScores(key, start, end);
    }

    /**
     * 读取 key
     *
     * @param key 键
     * @param min 最小分数
     * @param max 最大分数
     * @return 值
     */
    public Set<Object> zRangeByScore(String key, double min, double max) {
        return redisTemplate.opsForZSet().rangeByScore(key, min, max);
    }

    /**
     * 删除 key
     *
     * @param key    键
     * @param values 删除的成员
     * @return 删除的个数
     */
    public Long zRemove(String key, Object... values) {
        if (values == null || values.length == 0) {
            return 0L;
        }
        return redisTemplate.opsForZSet().remove(key, values);
    }

    /**
     * 获取 key 的长度
     *
     * @param key 键
     * @return 长度
     */
    public Long zSize(String key) {
        return redisTemplate.opsForZSet().zCard(key);
    }

    /**
     * 获取 key 的分数
     *
     * @param key   键
     * @param value 值
     * @return 分数
     */
    public Double zScore(String key, Object value) {
        return redisTemplate.opsForZSet().score(key, value);
    }
}
