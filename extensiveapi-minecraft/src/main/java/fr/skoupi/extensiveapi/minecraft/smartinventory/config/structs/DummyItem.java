package fr.skoupi.extensiveapi.minecraft.smartinventory.config.structs;

import lombok.Builder;
import lombok.Data;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Data
@Builder(setterPrefix = "set")
public class DummyItem {

    private int[] slot;
    private ItemStack item;
    private List<String> commands;

}
