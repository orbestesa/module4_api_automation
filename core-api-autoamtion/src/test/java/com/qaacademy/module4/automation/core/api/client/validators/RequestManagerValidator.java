package com.qaacademy.module4.automation.core.api.client.validators;


import com.qaacademy.module4.automation.core.utils.json.JsonPath;

import java.util.List;

public final class RequestManagerValidator {

    /**
     * Default private constructor.
     */
    private RequestManagerValidator() {
    }

    public static String getName(String json, String value) {
        return JsonPath.getResultList(json, String.format("$[?(@.id == '%s')].name", value)).get(0).toString();
    }

    public static String getBoardName(String json) {
        return JsonPath.getResult(json, "$.name");
    }

    public static boolean ExistId(String json, String id) {
        List<String> idList = JsonPath.getResultList(json,"$[0:].id").stream().map(String::valueOf).toList();
        return idList.contains(id);
    }

    public static String getId(String json) { return JsonPath.getResult(json, "$.id"); }
}
