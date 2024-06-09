package fr.skoupi.extensiveapi.minecraft.json;

/*  LiteLocation
 *  By: vSKAH <vskahhh@gmail.com>

 * Created with IntelliJ IDEA
 * For the project ExtensiveAPI
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import fr.skoupi.extensiveapi.minecraft.json.adapters.itemstack.ItemStackDeserializer;
import fr.skoupi.extensiveapi.minecraft.json.adapters.itemstack.ItemStackSerializer;
import fr.skoupi.extensiveapi.minecraft.json.adapters.location.LocationDeserializer;
import fr.skoupi.extensiveapi.minecraft.json.adapters.location.LocationSerializer;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public class MinecraftObjectMapper {

	@Getter
	private final SimpleModule simpleModule;

	@Setter
	private ObjectMapper objectMapper;

	public MinecraftObjectMapper ()
	{
		// It's creating a new instance of the SimpleModule class.
		simpleModule = new SimpleModule();
		// It's adding a new serializer and deserializer for the Location class.
		simpleModule.addSerializer(Location.class, new LocationSerializer());
		simpleModule.addDeserializer(Location.class, new LocationDeserializer());

		// It's adding a new serializer and deserializer for the ItemStack class.
		simpleModule.addSerializer(ItemStack.class, new ItemStackSerializer(ItemStack.class));
		simpleModule.addDeserializer(ItemStack.class, new ItemStackDeserializer());

		// It's creating a new instance of the ObjectMapper class.
		this.objectMapper = new ObjectMapper();
	}


	public ObjectMapper getObjectMapper ()
	{
		// It's registering the module to the object mapper.
		return objectMapper.registerModule(getSimpleModule());
	}
}
