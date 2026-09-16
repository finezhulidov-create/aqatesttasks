package dev.zhulidov.tests;

import dev.zhulidov.tests.support.BaseTest;
import io.qameta.allure.Allure;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Feature("POST /endpoint")
@Story("Валидация запроса")
public class RequestValidationTest extends BaseTest {
    private String action;
    private String body = "token="+ token +"&action="+action;

    @Test
    @Step("Отправка запроса на /endpoint с не верным X-Api-Key")
    @DisplayName("Hе верный API key должен вернуть 401")
    void invalidApiKey_shouldReturn401() throws IOException, InterruptedException {
        action = "LOGIN";
        String apiKey = "22222";

        HttpClient client = HttpClient.newHttpClient();
        Allure.addAttachment("Тело запроса", body);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/endpoint"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Accept", "application/json")
                .header("X-Api-Key", apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        HttpResponse<String> response = client.send(request,HttpResponse.BodyHandlers.ofString());
        Allure.addAttachment("Ответ статус ("+ response.statusCode()+")", response.body());
        assertEquals(401,response.statusCode());
    }

    @Step("Отправка запроса на /endpoint с отсутствующим X-Api-Key")
    @Test
    @DisplayName("Отсутствующий API key должен вернуть 401")
    void missingApiKey_shouldReturn401() throws IOException, InterruptedException {
        action = "LOGIN";

        HttpClient client = HttpClient.newHttpClient();
        Allure.addAttachment("Тело запроса", body);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/endpoint"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("token="+ token +"&action="+action))
                .build();
        HttpResponse<String> response = client.send(request,HttpResponse.BodyHandlers.ofString());
        Allure.addAttachment("Ответ статус ("+ response.statusCode()+")", response.body());
        assertEquals(401,response.statusCode());

    }
    @Step("Отправка запроса на /endpoint с невалидным token")
    @ParameterizedTest(name = "Возвращает 400, если токен \"{0}\" невалидный")
    @ValueSource(strings = {
            "AFHDNNGIGLKMN294942244556GHFJ6DH",
            "2222DKDKDJ",
            "abcdefabcdefabcdefabcdefabcdefab",
            "ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ",
            ""
    })

    void notValidToken_shouldReturn400(String invalidToken) throws IOException, InterruptedException {


        var response = sendEndpointRequest(invalidToken,"ACTION");

        assertEquals(400, response.statusCode());


    }

    @Step("Отправка запроса на /endpoint с отсутствующим token")
    @Test
    @DisplayName("Возвращает 400, если параметр token отсутствует")
    void ifTokenIsNull_shouldReturn400() throws IOException, InterruptedException {
         action = "ACTION";
         body = "&action="+action;
        HttpClient client = HttpClient.newHttpClient();
        Allure.addAttachment("Тело запроса", body);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/endpoint"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Accept", "application/json")
                .header("X-Api-Key", "qazWSXedc")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        HttpResponse<String> response = client.send(request,HttpResponse.BodyHandlers.ofString());
        Allure.addAttachment("Ответ статус ("+ response.statusCode()+")", response.body());
        assertEquals(400,response.statusCode());

    }
    @Step("Отправка запроса на /endpoint с отсутствующим action")
    @Test
    @DisplayName("Возвращает 400, если параметр action отсутствует")
    void ifActionIsNull_shouldReturn400() throws IOException, InterruptedException {
        body = "token="+ token;
        HttpClient client = HttpClient.newHttpClient();
        Allure.addAttachment("Тело запроса", body);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/endpoint"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Accept", "application/json")
                .header("X-Api-Key", "qazWSXedc")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        HttpResponse<String> response = client.send(request,HttpResponse.BodyHandlers.ofString());
        Allure.addAttachment("Ответ статус ("+ response.statusCode()+")", response.body());
        assertEquals(400,response.statusCode());

    }
    @Step("Отправка запроса на /endpoint с невалидным action")
    @ParameterizedTest(name = "Возвращает {1}, если невалидный параметр action =\"{0}\"")
    @CsvSource({
            "NoPE, 400",
            "action, 400",
            "'', 400"
    })

    void ifActionInvalid_shouldReturn400(String action, int expectedStatus) throws IOException, InterruptedException {

     var response =  sendEndpointRequest(token,action);
        assertEquals(expectedStatus,response.statusCode());

    }
}
