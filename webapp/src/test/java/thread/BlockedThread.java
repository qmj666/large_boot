package thread;

import com.large.LargeApplication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

@DisplayName("测试1")
@SpringBootTest(classes = LargeApplication.class)

public class BlockedThread implements Runnable {
    @Override
    public void run() {
        synchronized (BlockedThread.class){
//            while (true){
                System.out.println("BlockedThread");
                WaitingTime.waitSecond(15);
//            }
        }
    }
}
