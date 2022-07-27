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
public class FutureTaskTest {


    @Test
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        FutureTask<String> futureTask = new FutureTask<>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "测试FutureTask获取异步结果";
            }
        });
        executorService.execute(futureTask);
        System.out.println(futureTask.get().toString());
        executorService.shutdown();
    }
}
