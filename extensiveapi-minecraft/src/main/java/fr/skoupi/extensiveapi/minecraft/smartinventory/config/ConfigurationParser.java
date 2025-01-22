package fr.skoupi.extensiveapi.minecraft.smartinventory.config;

import fr.skoupi.extensiveapi.minecraft.itemstack.ItemBuilder;
import fr.skoupi.extensiveapi.minecraft.smartinventory.config.structs.DummyItem;
import fr.skoupi.extensiveapi.minecraft.smartinventory.config.structs.GuiSettings;
import fr.skoupi.extensiveapi.minecraft.smartinventory.config.structs.PaginationButton;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ConfigurationParser {


    /**
     * Parse a dummy item from a configuration section
     * What is a dummy item? A dummy item is an item that is displayed in the inventory but does not do anything when clicked.
     *
     * @param section The configuration section to parse
     * @return The parsed dummy item
     */
    public static @Nullable DummyItem parseDummyItem(@Nullable ConfigurationSection section) {
        if (section == null) return null;
        DummyItem.DummyItemBuilder builder = DummyItem.builder();
        builder.setSlot(section.getIntegerList("slots").stream().mapToInt(Integer::intValue).toArray());
        builder.setItem(ItemBuilder.fromConfiguration(section.getConfigurationSection("item")));
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
    public static @Nullable PaginationButton parsePaginationButton(@Nullable ConfigurationSection section) {
        if (section == null) return null;
        PaginationButton.PaginationButtonBuilder builder = PaginationButton.builder();
        builder.setSlot(section.getInt("slot"));
        builder.setItem(ItemBuilder.fromConfiguration(section.getConfigurationSection("item")));
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
    public static @Nullable GuiSettings parseSettings(@Nullable ConfigurationSection section) {
        if (section == null) return null;
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
            builder.setIteratorItem(ItemBuilder.fromConfiguration(iterator.getConfigurationSection("item")));
        }
        return builder.build();
    }


}
