package thread.Future;

import com.large.LargeApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.*;

/**
 * @author binghe
 * @version 1.0.0
 * @description 测试FutureTask获取异步结果
 */
@SpringBootTest(classes = LargeApplication.class)
public class FutureTest {
    @Test
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<String> future = executorService.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "测试Future获取异步结果";
            }
        });
        System.out.println(future.get());
        executorService.shutdown();
    }


}
