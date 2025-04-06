package amateurs.mapper;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameCourtDeserializer extends JsonDeserializer<List<String>> {
    @Override
    public List<String> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        final ArrayNode arrayNode = jsonParser.getCodec().readTree(jsonParser);
        final List<String> courts = new ArrayList<>();
        for (JsonNode node : arrayNode) {
            courts.add(node.get("court").asText());
        }
        return courts;
    }
}
