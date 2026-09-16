package dev.zhulidov.tests;

import dev.zhulidov.tests.support.BaseTest;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.http.HttpResponse;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Feature("POST /endpoint")
@Story("ACTION")
public class ActionTest extends BaseTest {
    @Test
    @DisplayName("Экшн должен вернуть 200")
    void action_shouldReturn200() throws IOException, InterruptedException {
        sendEndpointRequest(token, "LOGIN");
        HttpResponse<String> response = sendEndpointRequest(token,"ACTION");
        assertEquals(200, response.statusCode());

    }

    @Test
    @DisplayName("500 ошибка, когда сервер не отвечает на экшн")
    void action_shouldReturn500IfServerError() throws IOException, InterruptedException {
        wireMockServer.stubFor(
                post(urlPathEqualTo("/doAction"))
                        .willReturn(aResponse().withStatus(500))
        );
        sendEndpointRequest(token,"LOGIN");
        var response = sendEndpointRequest(token,"ACTION");
        assertEquals(500,response.statusCode());

    }

    @Test
    @DisplayName("403 ошибка, если action без предварительного login")
    void action_shouldReturn403WithoutLogin() throws IOException, InterruptedException {
        var response = sendEndpointRequest(token,"ACTION");
        assertEquals(403,response.statusCode());
    }
}
