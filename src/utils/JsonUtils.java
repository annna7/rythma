package utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class JsonUtils {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static String toJson(List<String> data) {
        try {
            return mapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            System.err.println("Error converting list to JSON: " + e.getMessage());
            return "[]";
        }
    }

    public static List<String> fromJsonToList(String json) {
        try {
            return mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, String.class));
        } catch (JsonProcessingException e) {
            System.err.println("Error converting JSON to list: " + e.getMessage());
            return Collections.emptyList();
        } catch (Exception e) {
            System.err.println("Error converting JSON to list: " + e.getMessage());
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}