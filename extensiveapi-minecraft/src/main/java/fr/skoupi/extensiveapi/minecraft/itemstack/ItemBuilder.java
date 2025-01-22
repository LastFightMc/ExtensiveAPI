/*
 * This file is part of FastInv, licensed under the MIT License.
 *
 * Copyright (c) 2018-2021 MrMicky
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package fr.skoupi.extensiveapi.minecraft.itemstack;

import fr.skoupi.extensiveapi.minecraft.ExtensiveCore;
import fr.skoupi.extensiveapi.minecraft.hooks.Hooks;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Simple {@link ItemStack} builder
 *
 * @author MrMicky
 */
public class ItemBuilder {

    private final ItemStack item;
    private final ItemMeta meta;

    public ItemBuilder(Material material) {
        this(new ItemStack(material));
    }

    public ItemBuilder(ItemStack item) {
        this.item = Objects.requireNonNull(item, "item");
        this.meta = item.getItemMeta();

        if (this.meta == null) {
            throw new IllegalArgumentException("The type " + item.getType() + " doesn't support item meta");
        }
    }

    public ItemBuilder type(Material material) {
        this.item.setType(material);
        return this;
    }

    public ItemBuilder data(int data) {
        return durability((short) data);
    }

    @SuppressWarnings("deprecation")
    public ItemBuilder durability(short durability) {
        this.item.setDurability(durability);
        return this;
    }

    public ItemBuilder amount(int amount) {
        this.item.setAmount(amount);
        return this;
    }

    public ItemBuilder enchant(Enchantment enchantment) {
        return enchant(enchantment, 1);
    }

    public ItemBuilder enchant(Enchantment enchantment, int level) {
        this.meta.addEnchant(enchantment, level, true);
        return this;
    }

    public ItemBuilder removeEnchant(Enchantment enchantment) {
        this.meta.removeEnchant(enchantment);
        return this;
    }

    public ItemBuilder removeEnchants() {
        this.meta.getEnchants().keySet().forEach(this.meta::removeEnchant);
        return this;
    }

    public ItemBuilder meta(Consumer<ItemMeta> metaConsumer) {
        metaConsumer.accept(this.meta);
        return this;
    }

    public <T extends ItemMeta> ItemBuilder meta(Class<T> metaClass, Consumer<T> metaConsumer) {
        if (metaClass.isInstance(this.meta)) {
            metaConsumer.accept(metaClass.cast(this.meta));
        }
        return this;
    }

    public ItemBuilder name(String name) {
        this.meta.setDisplayName(name);
        return this;
    }

    public ItemBuilder lore(String lore) {
        return lore(Collections.singletonList(lore));
    }

    public ItemBuilder lore(String... lore) {
        return lore(Arrays.asList(lore));
    }

    public ItemBuilder lore(List<String> lore) {
        this.meta.setLore(lore);
        return this;
    }

    public ItemBuilder addLore(String line) {
        List<String> lore = this.meta.getLore();

        if (lore == null) {
            return lore(line);
        }

        lore.add(line);
        return lore(lore);
    }

    public ItemBuilder addLore(String... lines) {
        return addLore(Arrays.asList(lines));
    }

    public ItemBuilder addLore(List<String> lines) {
        List<String> lore = this.meta.getLore();

        if (lore == null) {
            return lore(lines);
        }

        lore.addAll(lines);
        return lore(lore);
    }

    public ItemBuilder flags(ItemFlag... flags) {
        this.meta.addItemFlags(flags);
        return this;
    }

    public ItemBuilder flags() {
        return flags(ItemFlag.values());
    }

    public ItemBuilder removeFlags(ItemFlag... flags) {
        this.meta.removeItemFlags(flags);
        return this;
    }

    public ItemBuilder removeFlags() {
        return removeFlags(ItemFlag.values());
    }

    public ItemBuilder armorColor(Color color) {
        return meta(LeatherArmorMeta.class, m -> m.setColor(color));
    }

    public ItemStack build() {
        this.item.setItemMeta(this.meta);
        return this.item;
    }

    /**
     * Parse an itemstack from a yaml configuration section
     *
     * @param section The configuration section to parse
     * @return The parsed itemstack
     * @see ItemStack
     * @see ConfigurationSection
     */
    public static @Nullable ItemStack fromConfiguration(@Nullable ConfigurationSection section) {
        if (section == null) {
            ExtensiveCore.getInstance().getLogger().severe("Unable to build item from configuration in ItemBuilder because section is null");
            return null;
        }
        return ConfigurationParser.parseItem(section);
    }

    private static class ConfigurationParser {

        private static final boolean HDB_IS_LOADED = Hooks.getInstance().isHooked("HeadDatabase", false);

        public static ItemStack parseItem(ConfigurationSection parser) {
            ItemBuilder builder;
            if (parser == null) return new ItemBuilder(Material.STONE).name("§cError in configuration section").build();

            if (parser.contains("texture_base64")) {
                String texture = parser.getString("texture_base64", "");
                if (texture.isEmpty())
                    return new ItemBuilder(Material.STONE).name("§cError when loading head from texture").build();
                builder = new ItemBuilder(SkullCreator.itemFromBase64(texture));
            } else {
                String material = parser.getString("material", "");
                if (material.contains("hdb-") && HDB_IS_LOADED) {
                    HeadDatabaseAPI api = (HeadDatabaseAPI) Hooks.getInstance().getLoaded().get("HeadDatabase").getHook();
                    if (api == null)
                        return new ItemBuilder(Material.STONE).name("§cError when loading the HDB API").build();
                    ItemStack stack = api.getItemHead(material.substring(4));
                    if (stack == null)
                        return new ItemBuilder(Material.STONE).name("§cError when loading head from HDB").build();
                    builder = new ItemBuilder(stack);
                } else {
                    Material mat = Material.getMaterial(material);
                    if (mat == null)
                        return new ItemBuilder(Material.STONE).name("§cError when loading material").build();
                    builder = new ItemBuilder(mat);
                }
            }

            if (parser.contains("name"))
                builder.name(ChatColor.translateAlternateColorCodes('&', parser.getString("name", "LOADING_NAME_ERROR")));
            if (parser.contains("lore") && !parser.getStringList("lore").isEmpty())
                builder.lore(parser.getStringList("lore"));
            if (parser.contains("amount")) builder.amount(parser.getInt("amount"));
            if (parser.contains("data")) builder.data(parser.getInt("data"));
            if (parser.contains("durability")) builder.durability((short) parser.getInt("durability"));
            if (parser.contains("hide_attributes")) builder.flags(ItemFlag.HIDE_ATTRIBUTES);
            if (parser.contains("hide_enchants")) builder.flags(ItemFlag.HIDE_ENCHANTS);
            if (parser.contains("hide_all")) builder.flags();
            if (parser.contains("enchants")) {
                List<String> enchants = parser.getStringList("enchants");
                if (enchants != null && !enchants.isEmpty()) for (String enchantmentString : enchants) {
                    String[] enchantmentArray = enchantmentString.split(":");
                    if (enchantmentArray.length == 0) continue;
                    Enchantment enchantment = findEnchantmentByName(enchantmentArray[0]);
                    if (enchantment == null) continue;
                    if (enchantmentArray.length > 1)
                        builder.enchant(enchantment, Integer.parseInt(enchantmentArray[1]));
                    else builder.enchant(enchantment);
                }
            }
            return builder.build();
        }

        /**
         * Find an enchantment by its name
         *
         * @param enchantmentName The name of the enchantment
         * @return The enchantment if found, null otherwise
         */
        private static @Nullable Enchantment findEnchantmentByName(@NotNull String enchantmentName) {
            try {
                Enchantment enchant = Enchantment.getByName(enchantmentName);
                return enchant;
            } catch (Exception e) {
                ExtensiveCore.getInstance().getLogger().warning("Error when loading enchantment " + enchantmentName);
                return null;
            }
        }


    }
}

