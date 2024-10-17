/*  CraftingUtils
 * By: jimmy "vSKAH" <vskahhh@gmail.com>
 * Created with IntelliJ IDEA
 * For the project sStaff
 * 09/09/2024
 */

package fr.skoupi.extensiveapi.minecraft.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.material.MaterialData;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class CraftUtils {

    /*
     * Remove a recipe from itemStack
     * @param itemstack the Itemstack of the recipe
     *
     * @return The removed recipes
     */

    public static Recipe[] removeRecipe(ItemStack itemStack) {

        Set<Recipe> removedRecipes = new HashSet<>();
        Iterator<Recipe> recipeIterator = Bukkit.getServer().recipeIterator();

        while (recipeIterator.hasNext()) {
            Recipe recipe = recipeIterator.next();
            if (recipe != null && recipe.getResult().isSimilar(itemStack)) {
                removedRecipes.add(recipe);
                recipeIterator.remove();
            }
        }
        return removedRecipes.toArray(new Recipe[0]);
    }

    /*
     * Remove a recipe
     * @param material The material of the recipe
     * @param data The data of the recipe
     *
     * @return The removed recipes
     */

    public static Recipe[] removeRecipe(Material material, short data) {

        Set<Recipe> removedRecipes = new HashSet<>();
        Iterator<Recipe> recipeIterator = Bukkit.getServer().recipeIterator();

        while (recipeIterator.hasNext()) {
            Recipe recipe = recipeIterator.next();
            if (recipe != null && recipe.getResult().getType() == material &&
                    recipe.getResult().getDurability() == data) {
                removedRecipes.add(recipe);
                recipeIterator.remove();
            }
        }
        return removedRecipes.toArray(new Recipe[0]);
    }

    /*
     * Create a shaped recipe
     * @param resultMaterial The result material
     * @param shape The shape of the recipe
     * @param resultAmount The amount of the result
     * @param resultData The data of the result
     * @param ingredients The ingredients of the recipe
     *
     * @return The shaped recipe
     *
     * How to fill the ingredients array:
     * the first dimension is the key of the ingredient
     * the second dimension is the material and the data of the ingredient
     *
     */
    public static ShapedRecipe createShapedRecipe(ItemStack resultStack, String[] shape, String[][] ingredients) {
        char[] matrix = new char[9];
        for (String shapeLine : shape) {
            for (int i = 0; i < shapeLine.length(); i++) {
                matrix[i] = shapeLine.charAt(i);
            }
        }

        ShapedRecipe recipe = new ShapedRecipe(resultStack);
        recipe.shape(shape);

        for (int i = 0; i < ingredients.length; i++) {
            String key = ingredients[i][0];
            String materialAndData = ingredients[i][1];
            if (key == null || materialAndData == null) continue;
            String[] split;
            if (materialAndData.contains(":"))
                split = materialAndData.split(":");
            else
                split = new String[]{materialAndData};
            Material material = split.length == 1 ? Material.valueOf(materialAndData) : Material.valueOf(split[0]);
            byte data = (byte) split.length == 1 ? 0 : (byte) Integer.parseInt(split[1]);
            if (data != 0) {
                recipe.setIngredient(key.charAt(0), new MaterialData(material, data));
            } else {
                recipe.setIngredient(key.charAt(0), material);
            }
        }
        return recipe;
    }

    /*
     * Create a shapeless recipe
     * @param item The result item
     * @param ingredients The ingredients of the recipe
     *
     * @return The shapeless recipe
     *
     * How to fill the ingredients array:
     * the first dimension is the material and the data of the ingredient and the amount
     * example: "DIAMOND:0:2" -> 2 diamonds
     */

    public static ShapelessRecipe createShapelessRecipe(ItemStack item, String[] ingredients) {
        ShapelessRecipe recipe = new ShapelessRecipe(item);
        for (String ingredient : ingredients) {
            if (ingredient == null) continue;
            String[] split;
            if (ingredient.contains(":"))
                split = ingredient.split(":");
            else
                split = new String[]{ingredient};
            Material material = split.length == 1 ? Material.valueOf(ingredient) : Material.valueOf(split[0]);
            byte data = (byte) split.length == 2 ? 0 : (byte) Integer.parseInt(split[1]);
            int amount = split.length != 3 ? 1 : Integer.parseInt(split[2]);
            if (data != 0) recipe.addIngredient(amount, new MaterialData(material, data));
            else recipe.addIngredient(amount, material);
        }
        return recipe;
    }

}
