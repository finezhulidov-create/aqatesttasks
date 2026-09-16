package dev.zhulidov.tests;

import dev.zhulidov.tests.support.BaseTest;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.http.HttpResponse;


import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;


public class LoginTest extends BaseTest {

    @Test
    @DisplayName("Логин Должен вернуть 200 и ОК")
    void login_shouldReturn200AndOK() throws IOException, InterruptedException {

        HttpResponse<String> response = sendEndpointRequest(token,"LOGIN");
        assertEquals(200,response.statusCode());


    }

    @Test
    @DisplayName("Повторный логин должен вернуть 409")
    void retryLogin_shouldReturn409() throws IOException, InterruptedException {
        sendEndpointRequest(token,"LOGIN");
        HttpResponse<String> response = sendEndpointRequest(token,"LOGIN");
        assertEquals(409,response.statusCode());

    }

    @Test
    @DisplayName("500 ошибка, когда сервер не отвечает")
    void login_shouldReturn500IfInternalserverError() throws IOException, InterruptedException {
        wireMockServer.stubFor(
                post(urlPathEqualTo("/auth"))
                        .willReturn(aResponse().withStatus(500))
        );
        var response = sendEndpointRequest(token,"LOGIN");
        assertEquals(500,response.statusCode());

    }






}
