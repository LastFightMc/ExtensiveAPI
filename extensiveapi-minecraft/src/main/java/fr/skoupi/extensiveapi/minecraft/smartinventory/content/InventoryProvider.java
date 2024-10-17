package fr.skoupi.extensiveapi.minecraft.smartinventory.content;

import fr.skoupi.extensiveapi.minecraft.smartinventory.ClickableItem;
import fr.skoupi.extensiveapi.minecraft.smartinventory.config.structs.DummyItem;
import fr.skoupi.extensiveapi.minecraft.smartinventory.config.structs.GuiSettings;
import fr.skoupi.extensiveapi.minecraft.smartinventory.config.structs.PaginationButton;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public abstract class InventoryProvider {

    /**
     * Place dummy items in the inventory
     * @param player The player
     * @param contents The inventory contents
     * @param guiSettings The gui settings
     *
     * @see DummyItem
     * @see GuiSettings
     * @see fr.skoupi.extensiveapi.minecraft.smartinventory.config.ConfigurationParser
     *
     */
    protected void placeDummyItems(Player player, InventoryContents contents, GuiSettings guiSettings) {
        for (DummyItem dummyItem : guiSettings.getDummyItems()) {
            for (int slot : dummyItem.getSlot()) {
                SlotPos slotPos = new SlotPos(slot);
                if (dummyItem.getCommands() == null || dummyItem.getCommands().isEmpty())
                    contents.set(slotPos, ClickableItem.empty(dummyItem.getItem()));
                else
                    contents.set(slotPos, ClickableItem.of(dummyItem.getItem(), event -> {
                        for (String command : dummyItem.getCommands()) {
                            player.performCommand(command.replace("%player%", player.getName()));
                        }
                    }));
            }
        }
    }

    public void init(Player player, InventoryContents contents) {
    }


    public void update(Player player, InventoryContents contents) {
    }

    /**
     * Add pagination buttons to the inventory
     *
     * @param player The player
     * @param contents The inventory contents
     * @param settings The gui settings
     *
     * @see PaginationButton
     * @see GuiSettings
     * @see fr.skoupi.extensiveapi.minecraft.smartinventory.config.ConfigurationParser
     */
    protected void addPaginationButtons(Player player, InventoryContents contents, GuiSettings settings) {
        for (PaginationButton paginationButton : settings.getPaginationButtons()) {
            SlotPos slotPos = new SlotPos(paginationButton.getSlot());
            contents.set(slotPos, ClickableItem.of(paginationButton.getItem(), e -> {
                switch (paginationButton.getType()) {
                    case PREVIOUS_PAGE:
                        contents.inventory().open(player, contents.pagination().previous().getPage());
                        break;
                    case NEXT_PAGE:
                        contents.inventory().open(player, contents.pagination().next().getPage());
                        break;
                    case FIRST_PAGE:
                        contents.inventory().open(player, contents.pagination().first().getPage());
                        break;
                    case LAST_PAGE:
                        contents.inventory().open(player, contents.pagination().last().getPage());
                        break;
                    default:
                        Bukkit.getLogger().warning("Unknown pagination button type: " + paginationButton.getType());
                        break;
                }
            }));
        }
    }
}
