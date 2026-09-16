package dev.zhulidov.tests;

import dev.zhulidov.tests.support.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class RequestValidationTest extends BaseTest {
    @Test
    @DisplayName("Hе верный API key должен вернуть 401")
    void invalidApiKey_shouldReturn401() throws IOException, InterruptedException {
        String action = "LOGIN";
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/endpoint"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Accept", "application/json")
                .header("X-Api-Key", "22222")
                .POST(HttpRequest.BodyPublishers.ofString("token="+ token +"&action="+action))
                .build();
        HttpResponse<String> response = client.send(request,HttpResponse.BodyHandlers.ofString());
        assertEquals(401,response.statusCode());
    }

    @Test
    @DisplayName("Отсутствующий API key должен вернуть 401")
    void missingApiKey_shouldReturn401() throws IOException, InterruptedException {
        String action = "LOGIN";
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/endpoint"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("token="+ token +"&action="+action))
                .build();
        HttpResponse<String> response = client.send(request,HttpResponse.BodyHandlers.ofString());
        assertEquals(401,response.statusCode());

    }

    @Test
    @DisplayName("Возвращает 400, если токен не соответствует формату")
    void notValidToken_shouldReturn400() throws IOException, InterruptedException {

        token = "2222DKDKDJ";
        var response = sendEndpointRequest(token,"ACTION");

        assertEquals(400, response.statusCode());


    }

    @Test
    @DisplayName("Возвращает 400, если параметр token отсутствует")
    void ifTokenIsNull_shouldReturn400() throws IOException, InterruptedException {
        String action = "ACTION";
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/endpoint"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Accept", "application/json")
                .header("X-Api-Key", "qazWSXedc")
                .POST(HttpRequest.BodyPublishers.ofString("&action="+action))
                .build();
        HttpResponse<String> response = client.send(request,HttpResponse.BodyHandlers.ofString());
        assertEquals(400,response.statusCode());

    }

    @Test
    @DisplayName("Возвращает 400, если параметр action отсутствует")
    void ifActionIsNull_shouldReturn400() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/endpoint"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Accept", "application/json")
                .header("X-Api-Key", "qazWSXedc")
                .POST(HttpRequest.BodyPublishers.ofString("token="+ token))
                .build();
        HttpResponse<String> response = client.send(request,HttpResponse.BodyHandlers.ofString());

        assertEquals(400,response.statusCode());

    }

    @Test
    @DisplayName("Возвращает 400, если невалидный параметр action")
    void ifActionInvalid_shouldReturn400() throws IOException, InterruptedException {
        String action = "NoPE";
     var response =  sendEndpointRequest(token,action);
        assertEquals(400,response.statusCode());

    }
}
