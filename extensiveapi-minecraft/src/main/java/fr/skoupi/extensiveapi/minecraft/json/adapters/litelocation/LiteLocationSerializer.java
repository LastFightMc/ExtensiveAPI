package fr.skoupi.extensiveapi.minecraft.json.adapters.litelocation;

/*  LocationSerializer
 *  By: vSKAH <vskahhh@gmail.com>

 * Created with IntelliJ IDEA
 * For the project ExtensiveAPI
 */

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import fr.skoupi.extensiveapi.minecraft.liteobjects.LiteLocation;

import java.io.IOException;

public class LiteLocationSerializer extends StdSerializer<LiteLocation> {


    public LiteLocationSerializer() {
        this(null);
    }

    public LiteLocationSerializer(Class<LiteLocation> t) {
        super(t);
    }


    /**
     * "Write the location as a JSON object with the world name, x, y, z, yaw, and pitch."

     * The first line of the function is the annotation. This tells Jackson that this function is a serializer for the
     * Location class
     *
     * @param location The Location object that is being serialized.
     * @param jsonGenerator The JsonGenerator object that will be used to write the JSON.
     * @param serializerProvider This is the serializer provider.
     */
    @Override
    public void serialize(LiteLocation location, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        // Writing the JSON object.
        if(location.getWorldName() == null) return;
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("world", location.getWorldName());
        jsonGenerator.writeNumberField("x", location.getX());
        jsonGenerator.writeNumberField("y", location.getY());
        jsonGenerator.writeNumberField("z", location.getZ());
        jsonGenerator.writeEndObject();
    }

}
