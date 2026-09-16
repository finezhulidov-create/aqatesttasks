package dev.zhulidov.tests.support;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Random;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;

public abstract class BaseTest {
   protected String token;
    protected static WireMockServer wireMockServer;
    @BeforeAll
     static void startMockServer(){
        wireMockServer = new WireMockServer(8888);
        wireMockServer.start();

    }

    @AfterAll
     static void stopMockServer(){
        wireMockServer.stop();
    }

    @BeforeEach
     void resetStub(){
        token = generateToken();
        wireMockServer.resetAll();
        wireMockServer.stubFor(
                post(urlPathEqualTo("/auth"))
                        .willReturn(aResponse().withStatus(200)));
        wireMockServer.stubFor(
                post("/doAction")
                        .willReturn(aResponse().withStatus(200))
        );
    }

    private String generateToken(){
        String chars = "0123456789ABCDEF";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 32; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    protected HttpResponse<String> sendEndpointRequest(String token, String action) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/endpoint"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Accept", "application/json")
                .header("X-Api-Key", "qazWSXedc")
                .POST(HttpRequest.BodyPublishers.ofString("token="+token+"&action="+action))
                .build();
        return client.send(request,HttpResponse.BodyHandlers.ofString());
    }
}
