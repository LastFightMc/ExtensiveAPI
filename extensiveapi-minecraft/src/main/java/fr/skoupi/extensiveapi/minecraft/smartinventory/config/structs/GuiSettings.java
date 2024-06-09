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

    private String name;
    private int rows;

    private int[] iteratorSlots;
    private ItemStack iteratorItem;

    private DummyItem[] dummyItems;
    private PaginationButton[] paginationButtons;

}
