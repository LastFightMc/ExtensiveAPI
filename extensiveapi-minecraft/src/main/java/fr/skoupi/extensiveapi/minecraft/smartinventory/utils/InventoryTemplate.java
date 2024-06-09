package fr.skoupi.extensiveapi.minecraft.smartinventory.utils;

/*  CraftingUtils
 *  By: vSKAH <vskahhh@gmail.com>

 * Created with IntelliJ IDEA
 * For the project ExtensiveAPI
 */


import fr.skoupi.extensiveapi.minecraft.itemstack.ItemBuilder;
import fr.skoupi.extensiveapi.minecraft.smartinventory.ClickableItem;
import fr.skoupi.extensiveapi.minecraft.smartinventory.content.InventoryContents;
import fr.skoupi.extensiveapi.minecraft.smartinventory.content.SlotIterator;
import fr.skoupi.extensiveapi.minecraft.smartinventory.content.SlotPos;
import org.bukkit.Material;

public class InventoryTemplate {
	public static void addCornersTemplate (InventoryContents contents, Material mat, int data, String name)
	{
		contents.set(SlotPos.of(0, 0), ClickableItem.empty((new ItemBuilder(mat)).data(data).name(name).build()));
		contents.set(SlotPos.of(0, 1), ClickableItem.empty((new ItemBuilder(mat)).data(data).name(name).build()));
		contents.set(SlotPos.of(1, 0), ClickableItem.empty((new ItemBuilder(mat)).data(data).name(name).build()));
		contents.set(SlotPos.of(0, 7), ClickableItem.empty((new ItemBuilder(mat)).data(data).name(name).build()));
		contents.set(SlotPos.of(0, 8), ClickableItem.empty((new ItemBuilder(mat)).data(data).name(name).build()));
		contents.set(SlotPos.of(1, 8), ClickableItem.empty((new ItemBuilder(mat)).data(data).name(name).build()));
		contents.set(SlotPos.of(4, 0), ClickableItem.empty((new ItemBuilder(mat)).data(data).name(name).build()));
		contents.set(SlotPos.of(5, 0), ClickableItem.empty((new ItemBuilder(mat)).data(data).name(name).build()));
		contents.set(SlotPos.of(5, 1), ClickableItem.empty((new ItemBuilder(mat)).data(data).name(name).build()));
		contents.set(SlotPos.of(4, 8), ClickableItem.empty((new ItemBuilder(mat)).data(data).name(name).build()));
		contents.set(SlotPos.of(5, 8), ClickableItem.empty((new ItemBuilder(mat)).data(data).name(name).build()));
		contents.set(SlotPos.of(5, 7), ClickableItem.empty((new ItemBuilder(mat)).data(data).name(name).build()));
	}

	public static void blacklistTemplatePos (SlotIterator slotIterator)
	{
		slotIterator.blacklist(SlotPos.of(2, 7));
		slotIterator.blacklist(SlotPos.of(2, 8));

		slotIterator.blacklist(SlotPos.of(3, 0));
		slotIterator.blacklist(SlotPos.of(3, 1));
	}

}
