package fr.skoupi.extensiveapi.minecraft.smartinventory.config;

import fr.skoupi.extensiveapi.minecraft.ExtensiveCore;
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
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ConfigurationParser {

    private static boolean HDB_IS_LOADED = Hooks.getInstance().isHooked("HeadDatabase", true);

    /**
     * Parse an itemstack from a yaml configuration section
     *
     * @param parser The configuration section to parse
     * @return The parsed itemstack
     * @see ItemStack
     * @see ConfigurationSection
     */
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
            builder.name(ChatColor.translateAlternateColorCodes('&', parser.getString("name", "LOADING_NAME_ERROR")));
        if (parser.contains("lore") && parser.getStringList("lore") != null && !parser.getStringList("lore").isEmpty())
            builder.lore(parser.getStringList("lore"));
        if (parser.contains("amount"))
            builder.amount(parser.getInt("amount"));
        if (parser.contains("data"))
            builder.data(parser.getInt("data"));
        if (parser.contains("durability"))
            builder.durability((short) parser.getInt("durability"));
        if (parser.contains("hide_attributes"))
            builder.flags(ItemFlag.HIDE_ATTRIBUTES);
        if (parser.contains("hide_enchants"))
            builder.flags(ItemFlag.HIDE_ENCHANTS);
        if (parser.contains("enchants")) {
            List<String> enchants = parser.getStringList("enchants");
            if (enchants != null)
                for (String enchantmentString : enchants) {
                    String[] enchantmentArray = enchantmentString.split(":");
                    if (enchantmentArray.length == 0)
                        continue;
                    Enchantment enchantment = findEnchantmentByName(enchantmentArray[0]);
                    if (enchantment == null)
                        continue;
                    if (enchantmentArray.length > 1)
                        builder.enchant(enchantment, Integer.parseInt(enchantmentArray[1]));
                    else
                        builder.enchant(enchantment);
                }
        }
        return builder.build();
    }

    /**
     * Find an enchantment by its name
     *
     * @param enchantmentName The name of the enchantment
     * @return The enchantment if found, null otherwise
     */
    private static Enchantment findEnchantmentByName(String enchantmentName) {
        try {
            Enchantment enchant = Enchantment.getByName(enchantmentName);
            return enchant;
        } catch (Exception e) {
            ExtensiveCore.getInstance().getLogger().warning("Error when loading enchantment " + enchantmentName);
            return null;
        }
    }

    /**
     * Parse a dummy item from a configuration section
     * What is a dummy item? A dummy item is an item that is displayed in the inventory but does not do anything when clicked.
     *
     * @param section The configuration section to parse
     * @return The parsed dummy item
     */
    public static DummyItem parseDummyItem(ConfigurationSection section) {
        DummyItem.DummyItemBuilder builder = DummyItem.builder();
        builder.setSlot(section.getIntegerList("slots").stream().mapToInt(Integer::intValue).toArray());
        builder.setItem(parseItem(section.getConfigurationSection("item")));
        builder.setCommands(section.getStringList("commands"));
        return builder.build();
    }

    /**
     * Parse a pagination button from a configuration section
     * A pagination button is a button that allows the user to navigate through pages in a paginated inventory
     *
     * @param section The configuration section to parse
     * @return The parsed pagination button
     */
    public static PaginationButton parsePaginationButton(ConfigurationSection section) {
        PaginationButton.PaginationButtonBuilder builder = PaginationButton.builder();
        builder.setSlot(section.getInt("slot"));
        builder.setItem(parseItem(section.getConfigurationSection("item")));
        builder.setType(PaginationButton.PaginationButtonType.valueOf(section.getString("type", "NEXT_PAGE")));
        //builder.setAlwaysVisible(section.getBoolean("always_visible", true));
        return builder.build();
    }

    /**
     * Parse a gui settings from a configuration section
     * A gui settings is a set of settings that define how a gui should be displayed and what it should contain
     *
     * @param section The configuration section to parse
     * @return The parsed gui settings
     */
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
