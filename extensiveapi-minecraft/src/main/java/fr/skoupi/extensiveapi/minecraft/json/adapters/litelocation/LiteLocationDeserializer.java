package fr.skoupi.extensiveapi.minecraft.json.adapters.litelocation;

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
import fr.skoupi.extensiveapi.minecraft.liteobjects.LiteLocation;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.io.IOException;

public class LiteLocationDeserializer extends JsonDeserializer<LiteLocation> {

    /**
     * It reads the JSON tree, and returns a new LiteLocation object with the values from the JSON tree
     *
     * @param jsonParser The JsonParser that is used to parse the JSON.
     * @param deserializationContext The context of the deserialization.
     * @return A LiteLocation object
     */
    @Override
    public LiteLocation deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        final ObjectCodec objectCodec = jsonParser.getCodec();
        final JsonNode jsonNode = objectCodec.readTree(jsonParser);
        return new LiteLocation(jsonNode.get("world").textValue(), jsonNode.get("x").doubleValue(), jsonNode.get("y").doubleValue(), jsonNode.get("z").doubleValue());
    }
}
