package dev.zhulidov.tests;

import dev.zhulidov.tests.support.BaseTest;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import org.junit.jupiter.api.Test;


import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;
@Feature("POST /endpoint")
@Story("Конкурентный доступ")
public class ConcurrencyTest extends BaseTest {

    @Test
    @Step("Конкурентный LOGIN одним токеном — успеть должен только один")
    void concurrentLogin_onlyOneShouldSucceed() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(8);

        Callable<Integer> loginTask = () -> sendEndpointRequest(token,"LOGIN").statusCode();
        List<Future<Integer>> futures = executor.invokeAll(Collections.nCopies(8,loginTask));
        List<Integer> statuses = futures.stream()
                .map(fu-> {
                    try {
                        return fu.get();
                    } catch (InterruptedException | ExecutionException e) {
                        throw new RuntimeException(e);
                    }
                }).sorted().toList();
        executor.shutdown();
        Long count = statuses.stream().filter(i-> i==200).count();
        Long count409 = statuses.stream().filter(i-> i==409).count();
        assertEquals(1, count);
        assertEquals(7,count409);
    }

    @Test
    @Step("Конкуррентный LOGOUT должен вернуть только один 200")
    void concurrentLogout_shouldReturnOnlyOne200() throws IOException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(8);
        sendEndpointRequest(token,"LOGIN");
        Callable<Integer> logoutTask = () -> sendEndpointRequest(token,"LOGOUT").statusCode();
        List<Future<Integer>> futures = executor.invokeAll(Collections.nCopies(8,logoutTask));
        List<Integer> statuses = futures.stream()
                .map(fu -> {
                    try {
                        return fu.get();
                    } catch (InterruptedException | ExecutionException e) {
                        throw new RuntimeException(e);
                    }
                }).sorted().toList();
        executor.shutdown();
        Long count = statuses.stream().filter(i-> i == 200).count();
        Long count403 = statuses.stream().filter(i-> i == 403).count();
        assertEquals(1,count);
        assertEquals(7,count403);

    }
}
