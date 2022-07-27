package thread;

import com.large.LargeApplication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

@DisplayName("测试1")
@SpringBootTest(classes = LargeApplication.class)

public class WaitingState implements Runnable {
    @Override
    public void run() {
        while (true){
            synchronized (WaitingState.class){
                try {
                    System.out.println("WaitingState");
                    WaitingState.class.wait();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Test
    public static void main(String[] args){
        WaitingState runnable =   new WaitingState();
        runnable.run();
    }
}
