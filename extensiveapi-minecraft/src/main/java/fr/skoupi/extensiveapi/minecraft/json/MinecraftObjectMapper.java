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
import fr.skoupi.extensiveapi.minecraft.json.adapters.litechunk.LiteChunkDeserializer;
import fr.skoupi.extensiveapi.minecraft.json.adapters.litechunk.LiteChunkSerializer;
import fr.skoupi.extensiveapi.minecraft.json.adapters.litelocation.LiteLocationDeserializer;
import fr.skoupi.extensiveapi.minecraft.json.adapters.litelocation.LiteLocationSerializer;
import fr.skoupi.extensiveapi.minecraft.json.adapters.location.LocationDeserializer;
import fr.skoupi.extensiveapi.minecraft.json.adapters.location.LocationSerializer;
import fr.skoupi.extensiveapi.minecraft.liteobjects.LiteChunk;
import fr.skoupi.extensiveapi.minecraft.liteobjects.LiteLocation;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

@Getter
public class MinecraftObjectMapper {

	private final SimpleModule simpleModule;

	private ObjectMapper objectMapper;

	/**
	 * Constructor of the MinecraftObjectMapper class.
	 *
	 * @see SimpleModule
	 * @see ObjectMapper
	 */
	public MinecraftObjectMapper ()
	{
		// It's creating a new instance of the SimpleModule class.
		simpleModule = new SimpleModule();

		// It's adding a new serializer and deserializer for the Location class.
		simpleModule.addSerializer(Location.class, new LocationSerializer(Location.class));
		simpleModule.addDeserializer(Location.class, new LocationDeserializer());

		// It's adding a new serializer and deserializer for the LiteLocation class.
		simpleModule.addSerializer(LiteLocation.class, new LiteLocationSerializer(LiteLocation.class));
		simpleModule.addDeserializer(LiteLocation.class, new LiteLocationDeserializer());

		// It's adding a new serializer and deserializer for the ItemStack class.
		simpleModule.addSerializer(ItemStack.class, new ItemStackSerializer(ItemStack.class));
		simpleModule.addDeserializer(ItemStack.class, new ItemStackDeserializer());

		simpleModule.addSerializer(LiteChunk.class, new LiteChunkSerializer());
		simpleModule.addDeserializer(LiteChunk.class, new LiteChunkDeserializer());

		// It's creating a new instance of the ObjectMapper class.
		this.objectMapper = new ObjectMapper();
		this.objectMapper.registerModule(simpleModule);
	}

	/**
	 * It's refreshing the ObjectMapper instance.
	 */
	public void refreshMapper()
	{
		this.objectMapper = new ObjectMapper();
		this.objectMapper.registerModule(simpleModule);
	}
}
