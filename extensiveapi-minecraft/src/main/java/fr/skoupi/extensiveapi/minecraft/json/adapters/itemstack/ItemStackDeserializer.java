package fr.skoupi.extensiveapi.minecraft.json.adapters.itemstack;

/*  ItemStackDeserializer
 *  By: vSKAH <vskahhh@gmail.com>

 * Created with IntelliJ IDEA
 * For the project ExtensiveAPI
 */

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.skoupi.extensiveapi.minecraft.ExtensiveCore;
import fr.skoupi.extensiveapi.minecraft.utils.MinecraftVersion;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.io.IOException;
import java.util.*;

public class ItemStackDeserializer extends JsonDeserializer<ItemStack> {

    /**
     * It deserializes the itemstack from the json file
     *
     * @param jsonParser             The JsonParser that is used to parse the JSON.
     * @param deserializationContext The context of the deserialization.
     * @return ItemStack
     */
    @Override
    public ItemStack deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {

        final ObjectCodec objectCodec = jsonParser.getCodec();
        final JsonNode jsonNode = objectCodec.readTree(jsonParser);

        final ObjectMapper mapper = new ObjectMapper();

        // It creates a new ItemStack with the type and amount from the json file.
        final ItemStack stack = new ItemStack(Objects.requireNonNull(Material.getMaterial(jsonNode.get("type").asText())), jsonNode.get("amount").asInt(), jsonNode.has("stack_durability") ? (short) jsonNode.get("stack_durability").asInt() : 0);

        // It checks if the json file has a meta section.
        if (jsonNode.has("meta")) {
            // It converts the jsonNode to a Map.
            final Map<String, Object> itemMeta = mapper.treeToValue(jsonNode.get("meta"), Map.class);
            // Used to deserialize the ItemMeta.
            itemMeta.put("==", "ItemMeta");


            // It checks if the json file has a color section. If it has, it will create a new Color object with the red,
            // green and blue values.
            Color color = null;
            if (itemMeta.containsKey("color")) {
                if (jsonNode.get("meta").has("color")) {
                    JsonNode colorNode = jsonNode.get("meta").get("color");
                    color = Color.fromRGB(colorNode.get("red").intValue(), colorNode.get("green").intValue(), colorNode.get("blue").intValue());
                }
                itemMeta.remove("color");
            }

            ArrayList<ItemFlag> flags = new ArrayList<>();
            if (itemMeta.containsKey("ItemFlags")) {
               if (jsonNode.get("meta").has("ItemFlags"))
               {
                   JsonNode flagsNode = jsonNode.get("meta").get("ItemFlags");
                   flagsNode.forEach(jsonNode1 -> {
                       ItemFlag flag = ItemFlag.valueOf(jsonNode1.asText());
                       flags.add(flag);
                     });
               }
                itemMeta.remove("ItemFlags");
            }

            // It deserializes the ItemMeta from the json file.
            ItemMeta meta = (ItemMeta) ConfigurationSerialization.deserializeObject(itemMeta);
            // It clears the map.
            itemMeta.clear();

            for (ItemFlag flag : flags) {
                meta.addItemFlags(flag);
            }

            // It checks if the item is a leather armor and if the color is not null. If it is, it will set the color of
            // the leather armor.
            if (meta instanceof LeatherArmorMeta && color != null) {
                ((LeatherArmorMeta) meta).setColor(color);
            }

            // It checks if the json file has an attribute-modifiers section. If it has, it will add the attribute-modifiers
            // to the item.
            if (jsonNode.get("meta").has("attribute-modifiers")) {
                if (MinecraftVersion.atLeast(MinecraftVersion.V.v1_9)) {
                    jsonNode.get("meta").get("attribute-modifiers").forEach(jsonNode1 -> {
                        JsonNode node = jsonNode1.get(0);
                        final Map<String, Object> noded;
                        // It's just a try catch that will catch the JsonProcessingException.
                        try {
                            noded = mapper.treeToValue(node, Map.class);

                            // It's adding the attribute-modifiers to the item.
                            final AttributeModifier modifier = new AttributeModifier(UUID.fromString(noded.get("uniqueId").toString()), (String) noded.get("name"), (Double) noded.get("amount"), AttributeModifier.Operation.valueOf(noded.get("operation").toString().toUpperCase()), EquipmentSlot.valueOf(noded.get("slot").toString()));
                            final Attribute attribute = Attribute.valueOf(modifier.getName().toUpperCase().replace(".", "_"));
                            if (meta != null) meta.addAttributeModifier(attribute, modifier);
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }

                    });
                }
                // It's just a warning that will be displayed in the console if the server is not running on 1.9+
                else {
                    ExtensiveCore.getInstance().getLogger().warning("This method can be used only in 1.9+");
                }
            }
            // It's setting the ItemMeta to the ItemStack.
            stack.setItemMeta(meta);
        }
        return stack;
    }
}