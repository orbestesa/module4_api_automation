package com.qaacademy.module4.automation.trello.api.hooks;

import com.qaacademy.module4.automation.core.api.environment.EnvironmentManager;
import com.qaacademy.module4.automation.core.api.environment.client.ApiRequest;
import com.qaacademy.module4.automation.core.api.environment.client.ApiResponse;
import com.qaacademy.module4.automation.core.api.environment.client.RequestManager;
import com.qaacademy.module4.automation.trello.api.validator.RequestManagerValidator;
import io.cucumber.java.After;
import io.cucumber.java.Before;

import java.util.HashMap;
import java.util.Map;

public class BoardsHook {

    private final HashMap<String,String> context;
    private EnvironmentManager environmentManager;
    private ApiRequest apiRequest;
    private Map<String, String> params;
    private HashMap<String, String> headers;
    private ApiResponse response;

    public BoardsHook(final HashMap<String,String> context){
        this.context = context;
        environmentManager = EnvironmentManager.getInstance();
        params = new HashMap<>();
        params.put("key", environmentManager.getKey());
        params.put("token", environmentManager.getToken());
        headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        apiRequest = new ApiRequest();
    }
    @Before("@createBoard")
    public void createBoard(){
        var url = environmentManager.getUrl().concat("boards");
        apiRequest.setParams(params);
        apiRequest.setHeaders(headers);
        var boardName = "example-stef-2023-post";
        var body = """
                {
                    "name" : "%s"
                }""";
        apiRequest.setBody(String.format(body, boardName));
        response = RequestManager.post(apiRequest,url);
        var id = RequestManagerValidator.getId(response.getBody());
        context.put("boardName", boardName);
        context.put("boarId", id);
    }

    @After("@deleteBoard")
    public void deleteBoard(){
        var id = context.get("boarId");
        var url = environmentManager.getUrl().concat("boards/").concat(id);
        apiRequest.setParams(params);
        response = RequestManager.delete(apiRequest, url);
    }
}
