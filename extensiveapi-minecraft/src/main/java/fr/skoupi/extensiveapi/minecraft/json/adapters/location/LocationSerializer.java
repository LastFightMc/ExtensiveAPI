package fr.skoupi.extensiveapi.minecraft.json.adapters.location;

/*  LocationSerializer
 *  By: vSKAH <vskahhh@gmail.com>

 * Created with IntelliJ IDEA
 * For the project ExtensiveAPI
 */

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.bukkit.Location;

import java.io.IOException;

public class LocationSerializer extends JsonSerializer<Location> {

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
    public void serialize(Location location, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        // Writing the JSON object.
        if(location.getWorld() == null) return;
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("world", location.getWorld().getName());
        jsonGenerator.writeNumberField("x", location.getX());
        jsonGenerator.writeNumberField("y", location.getY());
        jsonGenerator.writeNumberField("z", location.getZ());
        jsonGenerator.writeNumberField("yaw", location.getYaw());
        jsonGenerator.writeNumberField("pitch", location.getPitch());
        jsonGenerator.writeEndObject();
    }

}
