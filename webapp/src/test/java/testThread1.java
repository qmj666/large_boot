import com.large.LargeApplication;
import com.large.utils.log4j2.LogUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

@DisplayName("测试1")
@SpringBootTest(classes = LargeApplication.class)

public class testThread1 {

    @Test
    public static void main(String[] args) throws InterruptedException {
        Thread thread1 = new Thread(() -> {
            System.out.println("thread1");
        });
        Thread thread2 = new Thread(() -> {
            System.out.println("thread2");
        });
        Thread thread3 = new Thread(() -> {
            System.out.println("thread3");
        });
        thread1.start();
        thread1.join();
        thread2.start();
        thread2.join();
        thread3.start();
        thread3.join();

    }
    public class CallableTest implements Callable<String> {
        @Override
        public String call() throws Exception {
//TODO 在此写在线程中执行的业务逻辑
            return null;
        }
    }
}
