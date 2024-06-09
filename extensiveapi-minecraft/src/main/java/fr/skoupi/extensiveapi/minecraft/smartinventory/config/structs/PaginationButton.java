package fr.skoupi.extensiveapi.minecraft.smartinventory.config.structs;

import lombok.Builder;
import lombok.Data;
import org.bukkit.inventory.ItemStack;

@Data
@Builder(setterPrefix = "set")
public class PaginationButton {

    private int slot;
    private ItemStack item;
    private PaginationButtonType type;
    private boolean alwaysVisible;

    public enum PaginationButtonType {
        PREVIOUS_PAGE,
        NEXT_PAGE,
        FIRST_PAGE,
        LAST_PAGE,
    }
}
