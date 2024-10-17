package fr.skoupi.extensiveapi.minecraft.utils;

/*  CraftingUtils
 *  By: vSKAH <vskahhh@gmail.com>

 * Created with IntelliJ IDEA
 * For the project ExtensiveAPI
 */

import fr.skoupi.extensiveapi.minecraft.ExtensiveCore;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Deprecated(forRemoval = true)
public class CraftingUtils {


    public void removeRecipe(Material material, short s) {
        final Iterator<Recipe> RECIPES_ITERATOR = Bukkit.getServer().recipeIterator();
        Recipe recipe;
        while (RECIPES_ITERATOR.hasNext()) {
            recipe = RECIPES_ITERATOR.next();
            if (recipe != null && recipe.getResult().getType() == material && recipe.getResult().getDurability() == s) {
                RECIPES_ITERATOR.remove();
                ExtensiveCore.getInstance().getLogger().info("Supression du craft de: " + recipe.getResult().getType());
            }
        }
    }


    public ShapedRecipe buildRecipe(String[] matrix, HashMap<Character, Material> map, ItemStack result) {
        ShapedRecipe recipe = new ShapedRecipe(result);
        recipe.shape(matrix);
        for (Map.Entry<Character, Material> ingredients : map.entrySet()) {
            recipe.setIngredient(ingredients.getKey(), ingredients.getValue());
        }
        return recipe;
    }

    public void registerRecipe(ShapedRecipe shapedRecipe) {
        Bukkit.getServer().addRecipe(shapedRecipe);
    }

}
