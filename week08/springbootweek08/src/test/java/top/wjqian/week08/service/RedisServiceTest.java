package top.wjqian.week08.service;

import com.alibaba.fastjson2.JSON;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import top.wjqian.week08.entity.Address;
import top.wjqian.week08.entity.User;
import java.util.Objects;

@SpringBootTest
@Slf4j
class RedisServiceTest {

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    public void testRedisTemplate() throws Exception{
//        stringRedisTemplate.opsForValue().set("hello", "world");
//        stringRedisTemplate.opsForValue().set("code:13900002222","1234");
//        stringRedisTemplate.opsForValue().set("code:13900001111","1564");
//
//        String value=stringRedisTemplate.opsForValue().get("hello");
//        log.info("Redis字符串测试结果：{}",value);
//        String code=stringRedisTemplate.opsForValue().get("code:13900002222");
//        log.info("13900002222验证码测试结果：{}",code);
//        String code2=stringRedisTemplate.opsForValue().get("code:13900001111");
//        log.info("13900001111验证码测试结果：{}",code2);

//        redisTemplate.opsForValue().set("code:13900003333","0000");
//        String code=Objects.requireNonNull(redisTemplate.opsForValue().get("code:13900003333")).toString();
//        log.info("13900003333验证码测试结果：{}",code);

        Address address=new Address();
        address.setCity("南京市");
        address.setStreet("栖霞区羊山北路1号");
        address.setZipCode("210000");

        User user=new User();
        user.setName("张三");
        user.setAge(23);
        user.setEmail("zhangsan@163.com");
        user.setAddress(address);
        redisTemplate.opsForValue().set("user:001",user);

       // redisutil.set(key:"")

        Object userObj=redisTemplate.opsForValue().get("user:001");

        User user2= JSON.parseObject(JSON.toJSONString(userObj), User.class);
        log.info("user:001测试结果：{}",user2);

    }
}