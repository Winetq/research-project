package agent.configuration.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;

public class Json {

    private static final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public static JsonNode parse(String source) throws JsonProcessingException {
        return objectMapper.readTree(source);
    }

    public static <T> T parseJsonNodeToString(JsonNode node, Class<T> classType) throws JsonProcessingException {
        return objectMapper.treeToValue(node, classType);
    }

    public static String stringify(JsonNode node) throws JsonProcessingException {
        return generateJson(node);
    }

    private static  String generateJson(Object o) throws JsonProcessingException {
        ObjectWriter writer = objectMapper.writer().with(SerializationFeature.INDENT_OUTPUT);
        return writer.writeValueAsString(o);
    }

    public static JsonNode parseObjectToJsonNode(Object obj) {
        return objectMapper.valueToTree(obj);
    }
}