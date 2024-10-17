package fr.skoupi.extensiveapi.minecraft.smartinventory.config.structs;

import lombok.Builder;
import lombok.Data;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Data
@Builder(setterPrefix = "set")
/*
 * Represents a dummy item in the inventory
 */
public class DummyItem {

    /**
     * The slot(s) where the item will be placed
     */
    private int[] slot;
    /*
     * The itemstack to place
     */
    private ItemStack item;

    /*
     * The commands to execute when the item is clicked
     * This can be null or empty if no command is to be executed
     * The commands can contain %player% which will be replaced by the player's name
     */
    private List<String> commands;

}
