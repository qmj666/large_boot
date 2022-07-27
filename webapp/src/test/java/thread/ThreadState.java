package thread;

import com.large.LargeApplication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

@DisplayName("测试1")
@SpringBootTest(classes = LargeApplication.class)
public class ThreadState{

    @Test
    public static void main(String[] args){
        new Thread(new WaitingTime(), "WaitingTimeThread").start();
      Thread thread =   new Thread(new WaitingState(), "WaitingStateThread");
        thread.start();
//        WaitingTime.waitSecond(15);


        System.out.println(thread.getState());
//BlockedThread-01线程会抢到锁，BlockedThread-02线程会阻塞
        new Thread(new BlockedThread(), "BlockedThread-01").start();
        new Thread(new BlockedThread(), "BlockedThread-02").start();
    }
}
