package com.qaacademy.module4.automation.trello.api.validator;

import com.qaacademy.module4.automation.core.utils.json.JsonPath;

public final class RequestManagerValidator {
    private RequestManagerValidator(){

    }

    public static String getBoardName(String json, String field, String value) {
        return JsonPath.getResultList(json, String.format("$[?(@.%s == '%s')].".concat(field), field, value)).get(0).toString();
    }
}
