package com.valanse.valanse.util;

import java.util.HashMap;
import java.util.Map;

public class DataParseUtil {

    public static Map<String, String> parseCommaSeparatedKeyValuePairs(String data) {
        Map<String, String> resultMap = new HashMap<>();
        String[] parts = data.split(",");

        for (String part : parts) {
            String[] keyValue = part.split(":");
            if (keyValue.length == 2) {
                resultMap.put(keyValue[0].trim(), keyValue[1].trim());
            }
        }

        return resultMap;
    }
}
