package dev.zhulidov.tests;

import dev.zhulidov.tests.support.BaseTest;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Feature("POST /endpoint")
@Story("LOGOUT")
public class LogoutTest extends BaseTest     {
    @Test
    @DisplayName("Логаут должен вернуть 200 ")
    void logoutShouldReturn200() throws IOException, InterruptedException {
        sendEndpointRequest(token,"LOGIN");
        HttpResponse<String> response = sendEndpointRequest(token,"LOGOUT");
        assertEquals(200,response.statusCode());



    }

    @Test
    @DisplayName("Логаут должен удалить токен  и вернуть 403 ")
    void logout_shouldReturn403AndRemoveToken() throws IOException, InterruptedException {
        sendEndpointRequest(token,"LOGIN");
        sendEndpointRequest(token,"LOGOUT");
        var responsAct = sendEndpointRequest(token, "ACTION");
        assertEquals(403,responsAct.statusCode());

    }
}
