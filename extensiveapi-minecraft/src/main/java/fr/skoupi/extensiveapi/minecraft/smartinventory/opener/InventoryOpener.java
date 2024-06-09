package fr.skoupi.extensiveapi.minecraft.smartinventory.opener;

import fr.skoupi.extensiveapi.minecraft.smartinventory.ClickableItem;
import fr.skoupi.extensiveapi.minecraft.smartinventory.SmartInventory;
import fr.skoupi.extensiveapi.minecraft.smartinventory.content.InventoryContents;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public interface InventoryOpener {

    Inventory open(SmartInventory inv, Player player);
    boolean supports(InventoryType type);

    default void fill(Inventory handle, InventoryContents contents) {
        ClickableItem[][] items = contents.all();

        for(int row = 0; row < items.length; row++) {
            for(int column = 0; column < items[row].length; column++) {
                if(items[row][column] != null)
                    handle.setItem(9 * row + column, items[row][column].getItem());
            }
        }
    }

}
