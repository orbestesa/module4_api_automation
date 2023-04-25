package com.qaacademy.module4.automation.core.api.client;

import com.qaacademy.module4.automation.core.api.client.validators.RequestManagerValidator;
import com.qaacademy.module4.automation.core.api.environment.EnvironmentManager;
import com.qaacademy.module4.automation.core.api.environment.client.ApiRequest;
import com.qaacademy.module4.automation.core.api.environment.client.ApiResponse;
import com.qaacademy.module4.automation.core.api.environment.client.RequestManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

@DisplayName("Request Manager")
@Tag("UnitTest")
public class RequestManagerTest {

    public static final int STATUS_CODE_SUCCESS = 200;
    private static String url;
    private static HashMap<String, String> params;
    private static HashMap<String, String> headers;
    private ApiRequest apiRequest;

    @BeforeAll
    public static void setupAll() {
        var environmentManager = EnvironmentManager.getInstance();
        url = environmentManager.getBaseUrl();
        String key = environmentManager.getKey();
        String token = environmentManager.getToken();

        params = new HashMap<>();
        params.put("key", key);
        params.put("token", token);

        headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
    }

    @BeforeEach
    public void setup() {
        apiRequest = new ApiRequest();
    }

    @Test
    @DisplayName("Verifies if GET method call correctly")
    public void verifiesIfGetMethodCallCorrectly() {
        // Given
        var endpoint = url.concat("1/members/me/boards");
        apiRequest.setParams(params);
        var id = "6441a462cd7ad0658941245b";

        // When
        var apiResponse = RequestManager.get(apiRequest, endpoint);

        // Then
        Assertions.assertEquals(STATUS_CODE_SUCCESS, apiResponse.getStatusCode(),
                String.format("Response status code is not 200. Response status cade: %s", apiResponse.getStatusCode()));
        Assertions.assertAll(
                () -> Assertions.assertTrue(apiResponse.getBody().contains("members"),
                        String.format("Response body does not contain 'members' string. Response id: %s", id)),
                () -> Assertions.assertEquals("example-test-stef4", RequestManagerValidator.getName(apiResponse.getBody(), id),
                        String.format("Response body does not contain 'example-test-stef4' string. Response body id: %s", id))
        );
    }

    @Test
    @DisplayName("Verifies if POST method call correctly")
    public void verifiesIfPostMethodCallCorrectly() {
        // Given
        var endpoint = url.concat("1/boards");
        var boardName = "Automation-test-03";
        var body = """
                {
                    "name" : "%s"
                }""";
        apiRequest.setParams(params);
        apiRequest.setBody(String.format(body, boardName));
        apiRequest.setHeaders(headers);


        // When
        ApiResponse apiResponse = RequestManager.post(apiRequest, endpoint);

        // Then
        Assertions.assertEquals(STATUS_CODE_SUCCESS, apiResponse.getStatusCode(),
                String.format("Response status code is not 200. Response status cade: %s", apiResponse.getStatusCode()));
        Assertions.assertAll(
                () -> Assertions.assertTrue(apiResponse.getBody().contains("members"),
                        String.format("Response body does not contain 'members' string. Response body: %s", apiResponse.getBody())),
                () -> Assertions.assertEquals(boardName, RequestManagerValidator.getBoardName(apiResponse.getBody()),
                        String.format("Response body does not contain '%s' string. Response body: %s", boardName, apiResponse.getBody()))
        );
    }

    @Test
    @DisplayName("Verifies if DELETE method call correctly")
    public void verifiesIfDelMethodCallCorrectly() {
        // Given
        var id = "6446c5b109c43ae839e47932";
        var endpoint = url.concat("1/boards/"+id);
        var boardName = "Automation-test03";
        apiRequest.setParams(params);
        apiRequest.setHeaders(headers);


        // When
        ApiResponse apiResponse = RequestManager.del(apiRequest, endpoint);
        apiRequest = new ApiRequest();
        var endpoint1 = url.concat("1/members/me/boards");
        apiRequest.setParams(params);
        ApiResponse apiResponse1 = RequestManager.get(apiRequest, endpoint1);

        // Then
        Assertions.assertEquals(STATUS_CODE_SUCCESS, apiResponse.getStatusCode(),
                String.format("Response status code is not 200. Response status cade: %s", apiResponse.getStatusCode()));
        Assertions.assertAll(
                () -> Assertions.assertTrue(apiResponse1.getBody().contains("members"),
                        String.format("Response body does not contain 'members' string. Response body: %s", apiResponse1.getBody())),
                () -> Assertions.assertFalse(RequestManagerValidator.ExistId(apiResponse1.getBody(), id),
                        String.format("Response body contain '%s' id. Id: %s, is not deleted", id, id))
        );
    }
}
