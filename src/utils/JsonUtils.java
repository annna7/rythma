package utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

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
            return Collections.emptyList();
        }
    }

    public static String mapToJson(Map<String, String> data) {
        try {
            return mapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            System.err.println("Error converting map to JSON: " + e.getMessage());
            return "{}";
        } catch (Exception e) {
            System.err.println("Unexpected error during JSON serialization: " + e.getMessage());
            return "{}";
        }
    }

    public static Map<String, String> fromJsonToMap(String json) {
        if (json == null) return new HashMap<>();
        try {
            return mapper.readValue(json, new TypeReference<Map<String, String>>() {});
        } catch (JsonProcessingException e) {
            System.err.println("Error converting JSON to map: " + e.getMessage());
            return Collections.emptyMap();
        } catch (Exception e) {
            System.err.println("Unexpected error during JSON deserialization: " + e.getMessage());
            e.printStackTrace();
            return Collections.emptyMap();
        }
    }
}