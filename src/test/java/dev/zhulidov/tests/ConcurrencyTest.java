package dev.zhulidov.tests;

import dev.zhulidov.tests.support.BaseTest;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import org.junit.jupiter.api.Test;


import java.util.List;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;
@Feature("POST /endpoint")
@Story("Конкурентный доступ")
public class ConcurrencyTest extends BaseTest {

    @Test
    @Step("Конкурентный LOGIN одним токеном — успеть должен только один")
    void concurrentLogin_onlyOneShouldSucceed() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        Callable<Integer> loginTask = () -> sendEndpointRequest(token,"LOGIN").statusCode();
        List<Future<Integer>> futures = executor.invokeAll(List.of(loginTask,loginTask));
        List<Integer> statuses = futures.stream()
                .map(fu-> {
                    try {
                        return fu.get();
                    } catch (InterruptedException | ExecutionException e) {
                        throw new RuntimeException(e);
                    }
                }).sorted().toList();
        executor.shutdown();
        assertEquals(List.of(200, 409),statuses);
    }
}
