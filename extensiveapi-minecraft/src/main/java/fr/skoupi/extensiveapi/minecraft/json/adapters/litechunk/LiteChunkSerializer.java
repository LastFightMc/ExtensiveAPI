package fr.skoupi.extensiveapi.minecraft.json.adapters.litechunk;

/*  LocationSerializer
 *  By: vSKAH <vskahhh@gmail.com>

 * Created with IntelliJ IDEA
 * For the project ExtensiveAPI
 */

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import fr.skoupi.extensiveapi.minecraft.liteobjects.LiteChunk;
import fr.skoupi.extensiveapi.minecraft.liteobjects.LiteLocation;

import java.io.IOException;

public class LiteChunkSerializer extends StdSerializer<LiteChunk> {


    public LiteChunkSerializer() {
        this(null);
    }

    public LiteChunkSerializer(Class<LiteChunk> t) {
        super(t);
    }


    /**
     * "Write the location as a JSON object with the world name, x, y, z, yaw, and pitch."
     * <p>
     * The first line of the function is the annotation. This tells Jackson that this function is a serializer for the
     * Location class
     *
     * @param chunk              The chunk object that is being serialized.
     * @param jsonGenerator      The JsonGenerator object that will be used to write the JSON.
     * @param serializerProvider This is the serializer provider.
     */
    @Override
    public void serialize(LiteChunk chunk, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        // Writing the JSON object.
        if (chunk.getWorldName() == null) return;
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("world", chunk.getWorldName());
        jsonGenerator.writeNumberField("x", chunk.getChunkX());
        jsonGenerator.writeNumberField("z", chunk.getChunkZ());
        jsonGenerator.writeEndObject();
    }

}
