package fr.skoupi.extensiveapi.minecraft.json.adapters.litechunk;

/*  LocationDeserializer
 *  By: vSKAH <vskahhh@gmail.com>

 * Created with IntelliJ IDEA
 * For the project ExtensiveAPI
 */

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import fr.skoupi.extensiveapi.minecraft.liteobjects.LiteChunk;
import fr.skoupi.extensiveapi.minecraft.liteobjects.LiteLocation;

import java.io.IOException;

public class LiteChunkDeserializer extends JsonDeserializer<LiteChunk> {

    /**
     * It reads the JSON tree, and returns a new LiteLocation object with the values from the JSON tree
     *
     * @param jsonParser The JsonParser that is used to parse the JSON.
     * @param deserializationContext The context of the deserialization.
     * @return A LiteLocation object
     */
    @Override
    public LiteChunk deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        final ObjectCodec objectCodec = jsonParser.getCodec();
        final JsonNode jsonNode = objectCodec.readTree(jsonParser);
        return LiteChunk.of(jsonNode.get("world").textValue(), jsonNode.get("x").intValue(), jsonNode.get("z").intValue());
    }
}
