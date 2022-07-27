package thread.Task;

import com.large.LargeApplication;
import org.springframework.boot.test.context.SpringBootTest;


public interface TaskCallable<T> {
    T callable(T t);
}
