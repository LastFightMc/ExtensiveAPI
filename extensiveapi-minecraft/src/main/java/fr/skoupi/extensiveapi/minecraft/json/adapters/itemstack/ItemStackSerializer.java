package fr.skoupi.extensiveapi.minecraft.json.adapters.itemstack;

/*  ItemStackSerializer
 *  By: vSKAH <vskahhh@gmail.com>

 * Created with IntelliJ IDEA
 * For the project ExtensiveAPI
 */

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import fr.skoupi.extensiveapi.minecraft.utils.MinecraftVersion;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ItemStackSerializer extends StdSerializer<ItemStack> {

    public ItemStackSerializer() {
        this(null);
    }

    public ItemStackSerializer(Class<ItemStack> t) {
        super(t);
    }

    /**
     * It serializes an ItemStack into a JSON object
     *
     * @param itemStack The ItemStack that is being serialized.
     * @param jsonGenerator The JsonGenerator object that will be used to write the JSON.
     * @param serializerProvider This is the serializer provider.
     */
    @Override
    public void serialize(ItemStack itemStack, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();

        // Writing the type of the itemstack into the JSON object.
        jsonGenerator.writeStringField("type", itemStack.getType().name());
        // It writes the amount of the itemstack into the JSON object.
        jsonGenerator.writeNumberField("amount", itemStack.getAmount());

        // It writes the durability of the itemstack into the JSON object.
        jsonGenerator.writeNumberField("stack_durability", itemStack.getDurability());

        // It checks if the itemstack has a meta.
        if (itemStack.hasItemMeta()) {

            // Serializing the ItemMeta into a Map.
            Map<String, Object> map = new HashMap<>(itemStack.getItemMeta().serialize());
            // It removes the lore from the map and adds it back as a list.
            if (itemStack.getItemMeta().hasLore()) {
                map.remove("lore");
                map.put("lore", itemStack.getItemMeta().getLore());
            }
            // It removes the display name from the map and adds it back as a string.
            if (itemStack.getItemMeta().hasDisplayName()) {
                map.remove("display-name");
                map.put("display-name", itemStack.getItemMeta().getDisplayName());
            }
            // It checks if the server is running on 1.9 or higher and if the itemstack has attribute modifiers. If it
            // does, it serializes them.
            if (MinecraftVersion.atLeast(MinecraftVersion.V.v1_9)) {
                if (itemStack.getItemMeta().getAttributeModifiers() != null && itemStack.getItemMeta().hasAttributeModifiers())
                    itemStack.getItemMeta().getAttributeModifiers().forEach((attribute, attributeModifier) -> attributeModifier.serialize());
            }
            // It writes the meta of the itemstack into the JSON object.
            jsonGenerator.writeObjectField("meta", map);
        }
        jsonGenerator.writeEndObject();

    }
}
