package fr.skoupi.extensiveapi.minecraft.smartinventory.config.structs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
@NoArgsConstructor
@Builder(setterPrefix = "set")
@Data
public class GuiSettings {

    /**
     * The name/title of the inventory
     */
    private String name;
    /**
     * The number of rows in the inventory
     */
    private int rows;

    /**
     * The slots where the iterator item will be placed
     */
    private int[] iteratorSlots;
    /**
     * The itemstack to use as an iterator
     * This item will be placed in the iterator slots
     * <p>
     * The iterator item is the item that will be used to add dynamic content to the inventory.
     * For example, if you want to display a list of players, the iterator item will be the item that will be repeated for each player.
     */
    private ItemStack iteratorItem;

    /**
     * The dummy items to place in the inventory
     */
    private DummyItem[] dummyItems;
    /**
     * The pagination buttons to place in the inventory
     */
    private PaginationButton[] paginationButtons;

}
