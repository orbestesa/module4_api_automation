package com.qaacademy.module4.automation.trello.api.steps;

import com.qaacademy.module4.automation.core.api.environment.EnvironmentManager;
import com.qaacademy.module4.automation.core.api.environment.client.ApiRequest;
import com.qaacademy.module4.automation.core.api.environment.client.ApiResponse;
import com.qaacademy.module4.automation.core.api.environment.client.RequestManager;
import com.qaacademy.module4.automation.trello.api.validator.RequestManagerValidator;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;

import java.util.HashMap;

public class BoardsSteps {
    private  final HashMap<String,String> context;
    private final EnvironmentManager environmentManager;
    private final HashMap<String , String> params;
    private final HashMap<String, String> headers;
    private final ApiRequest apiRequest;
    private ApiResponse response;

    public BoardsSteps(final HashMap<String,String> context){
        this.context = context;
        environmentManager = EnvironmentManager.getInstance();
        params = new HashMap<>();
        headers = new HashMap<>();
        headers.put("Content-Type","application/json");
        apiRequest = new ApiRequest();
    }

    @Given("I have a valid key and token")
    public void iHaveAValidKeyAndToken() {
        params.put("key", environmentManager.getKey());
        params.put("token", environmentManager.getToken());
    }
    @When("I sen a request to GET boards endpoint {string}")
    public void iSenARequestToGETBoardsEndpoint(String endpoint) {
        var url = environmentManager.getUrl().concat(endpoint);
        apiRequest.setParams(params);
        response = RequestManager.get(apiRequest,url);
    }

    @When("I send a request to GET a board endpoint {string}")
    public void iSendARequestToGETABoardEndpoint(String endpoint) {
        var id = context.get("boarId");
        var url = environmentManager.getUrl().concat(endpoint.replace("{id}", id));
        apiRequest.setParams(params);
        response = RequestManager.get(apiRequest,url);
    }

    @When("I send a request to POST a boards endpoint {string} with name {string}")
    public void iSendARequestToPOSTABoardsEndpointWithName(String endpoint, String name) {
        var url = environmentManager.getUrl().concat(endpoint);
        apiRequest.setParams(params);
        apiRequest.setHeaders(headers);
        var body = """
                {
                    "name" : "%s"
                }""";
        apiRequest.setBody(String.format(body, name));
        response = RequestManager.post(apiRequest, url);
        var id = RequestManagerValidator.getId(response.getBody());
        context.put("boardName", name);
        context.put("boarId", id);
    }

    @When("I sen a request to PUT a boards endpoint {string} with name {string}")
    public void iSenARequestToPUTABoardsEndpointWithName(String endpoint, String name) {
        var id = context.get("boarId");
        var url = environmentManager.getUrl().concat(endpoint.replace("{id}", id));
        apiRequest.setParams(params);
        apiRequest.setHeaders(headers);
        var body = """
                {
                    "name" : "%s"
                }""";
        apiRequest.setBody(String.format(body, name));
        response = RequestManager.put(apiRequest, url);
        context.put("boardName", name);
    }

    @When("I send a request to DELETE a boards endpoint {string}")
    public void iSendARequestToDELETEABoardsEndpoint(String endpoint) {
        var id = context.get("boarId");
        var url = environmentManager.getUrl().concat(endpoint.replace("{id}", id));
        apiRequest.setParams(params);
        response = RequestManager.delete(apiRequest, url);
    }

    @Then("The response status code should be {int}")
    public void theResponseStatusCodeShouldBe(int statusCode) {
        Assertions.assertEquals(statusCode,response.getStatusCode());
    }

    @And("The response should be contain {string} field")
    public void theResponseShouldBeContainField(String field) {
        Assertions.assertTrue(response.getBody().contains(field));
    }

    @And("The response should have a field {string} with value {string}")
    public void theResponseShouldHaveAFieldWithValue(String field, String value) {
        var boardName = context.get("boardName");
        value = value.replace("{name}", boardName);
        Assertions.assertEquals(value,RequestManagerValidator.getBoardName(response.getBody(), field, value));
    }
}
