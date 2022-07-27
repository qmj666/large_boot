package thread;

import com.large.LargeApplication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

@DisplayName("测试1")
@SpringBootTest(classes = LargeApplication.class)

public class WaitingTime implements Runnable{
    @Override
    public void run() {
        while (true){
            waitSecond(10);
        }
    }
    //线程等待多少秒
    public static final void waitSecond(long seconds){
        try {
            TimeUnit.SECONDS.sleep(seconds);
            System.out.println("WaitingTime");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public static void main(String[] args){
        WaitingTime runnable =   new WaitingTime();
        runnable.run();
    }
}
