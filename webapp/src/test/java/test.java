import com.large.LargeApplication;

import com.large.utils.log4j2.LogUtils;
import org.junit.jupiter.api.DisplayName;

import org.slf4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

@DisplayName("测试1")
@SpringBootTest(classes = LargeApplication.class)

public class test {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Test
    public void log4j2_test() {
        System.err.println("log4j2_test");
        Logger log1 = LogUtils.getBussinessLogger();
        Logger log2 = LogUtils.getDBLogger();
        Logger log3 = LogUtils.getExceptionLogger();
        Logger log4 = LogUtils.getPlatformLogger();
        log3.error("getExceptionLogger===日志测试");
        log3.error("getExceptionLogger===日志测试");
        log1.info("getBussinessLogger===日志测试");
        log2.info("getDBLogger===日志测试");
        log4.info("getPlatformLogger===日志测试");
    }

    @Test
    public void redis_test() {
        System.err.println("redis_test");
//        stringRedisTemplate.opsForValue().append("msg","coder");
        //列表操作
//        stringRedisTemplate.opsForList().leftPush("mylist","1");
//        stringRedisTemplate.opsForList().leftPush("mylist","2");
        String key1 = stringRedisTemplate.opsForValue().get("msg");
        String key2 = stringRedisTemplate.opsForList().index("mylist",1);
        System.out.println("key1: "+key1);
        System.out.println("key2: "+key2);
        stringRedisTemplate.expire("msg",2 , TimeUnit.MINUTES);
        stringRedisTemplate.expire("mylist",1 , TimeUnit.MINUTES);

    }

}
