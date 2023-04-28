package xyz.ibudai.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JsonUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static List<String[]> decodeJson(List<Serializable[]> data) {
        List<String[]> result = new ArrayList<>();
        try {
            for (Serializable[] row : data) {
                String json = objectMapper.writeValueAsString(row);
                json = json.replaceAll("\\\\u0000", "");
                String[] item = objectMapper.readValue(json, new TypeReference<String[]>() {
                });
                result.add(item);
                System.out.println(Arrays.toString(item));
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}
