package fr.skoupi.extensiveapi.minecraft.smartinventory.config;

import fr.skoupi.extensiveapi.minecraft.hooks.Hooks;
import fr.skoupi.extensiveapi.minecraft.itemstack.ItemBuilder;
import fr.skoupi.extensiveapi.minecraft.itemstack.SkullCreator;
import fr.skoupi.extensiveapi.minecraft.smartinventory.config.structs.DummyItem;
import fr.skoupi.extensiveapi.minecraft.smartinventory.config.structs.GuiSettings;
import fr.skoupi.extensiveapi.minecraft.smartinventory.config.structs.PaginationButton;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ConfigurationParser {

    private static boolean HDB_IS_LOADED = Hooks.getInstance().isHooked("HeadDatabase", true);

    public static ItemStack parseItem(ConfigurationSection parser) {
        ItemBuilder builder = null;
        if (parser == null)
            return new ItemBuilder(Material.STONE).name("§cError in configuration section").build();

        if (parser.contains("texture_base64")) {
            String texture = parser.getString("texture_base64", "");
            if (texture.isEmpty())
                return new ItemBuilder(Material.STONE).name("§cError when loading head from texture").build();
            builder = new ItemBuilder(SkullCreator.itemFromBase64(texture));
        } else {
            String material = parser.getString("material", "");
            if (material.contains("hdb-") && HDB_IS_LOADED) {
                HeadDatabaseAPI api = (HeadDatabaseAPI) Hooks.getInstance().getLoaded().get("HeadDatabase").getHook();
                if (api == null)
                    return new ItemBuilder(Material.STONE).name("§cError when loading the HDB API").build();
                ItemStack stack = api.getItemHead(material.substring(4));
                if (stack == null)
                    return new ItemBuilder(Material.STONE).name("§cError when loading head from HDB").build();
                builder = new ItemBuilder(stack);
            } else {
                Material mat = Material.getMaterial(material);
                if (mat == null)
                    return new ItemBuilder(Material.STONE).name("§cError when loading material").build();
                builder = new ItemBuilder(mat);
            }
        }

        if (parser.contains("name"))
            builder.name(ChatColor.translateAlternateColorCodes('&', parser.getString("name")));
        if (parser.contains("lore") && parser.getStringList("lore") != null && !parser.getStringList("lore").isEmpty())
            builder.lore(parser.getStringList("lore"));
        if (parser.contains("amount"))
            builder.amount(parser.getInt("amount"));
        if (parser.contains("data"))
            builder.data(parser.getInt("data"));
        if (parser.contains("durability"))
            builder.durability((short) parser.getInt("durability"));
        return builder.build();
    }

    public static DummyItem parseDummyItem(ConfigurationSection section) {
        DummyItem.DummyItemBuilder builder = DummyItem.builder();
        builder.setSlot(section.getIntegerList("slots").stream().mapToInt(Integer::intValue).toArray());
        builder.setItem(parseItem(section.getConfigurationSection("item")));
        builder.setCommands(section.getStringList("commands"));
        return builder.build();
    }

    public static PaginationButton parsePaginationButton(ConfigurationSection section) {
        PaginationButton.PaginationButtonBuilder builder = PaginationButton.builder();
        builder.setSlot(section.getInt("slot"));
        builder.setItem(parseItem(section.getConfigurationSection("item")));
        builder.setType(PaginationButton.PaginationButtonType.valueOf(section.getString("type", "NEXT_PAGE")));
        //builder.setAlwaysVisible(section.getBoolean("always_visible", true));
        return builder.build();
    }

    public static GuiSettings parseSettings(ConfigurationSection section) {
        GuiSettings.GuiSettingsBuilder builder = GuiSettings.builder();
        builder.setName(section.getString("title"));
        builder.setRows(section.getInt("rows"));
        ConfigurationSection dummys = section.getConfigurationSection("dummy_items");
        if (dummys != null) {
            List<DummyItem> dummyItems = new ArrayList<>();
            dummys.getKeys(false).forEach(key -> dummyItems.add(parseDummyItem(section.getConfigurationSection("dummy_items." + key))));
            builder.setDummyItems(dummyItems.toArray(new DummyItem[0]));
        }

        ConfigurationSection pagination = section.getConfigurationSection("pagination");
        if (pagination != null) {
            List<PaginationButton> paginationButtons = new ArrayList<>();
            pagination.getKeys(false).forEach(key -> paginationButtons.add(parsePaginationButton(pagination.getConfigurationSection(key))));
            builder.setPaginationButtons(paginationButtons.toArray(new PaginationButton[0]));
        }

        ConfigurationSection iterator = section.getConfigurationSection("iterator");
        if (iterator != null) {
            builder.setIteratorSlots(iterator.getIntegerList("slots").stream().mapToInt(Integer::intValue).toArray());
            builder.setIteratorItem(parseItem(iterator.getConfigurationSection("item")));
        }
        return builder.build();
    }


}
