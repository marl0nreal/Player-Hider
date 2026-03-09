package me.marlonreal.playerhider.util;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.stream.Collectors;

public class ItemBuilder {

    private final ItemStack item;
    private final ItemMeta meta;

    public ItemBuilder(Material material) {
        this.item = new ItemStack(material);
        this.meta = item.getItemMeta();
    }

    public ItemBuilder(ConfigurationSection section) {
        this(Material.valueOf(section.getString("material")));

        if (section.contains("display-name"))
            setDisplayName(section.getString("display-name"));

        if (section.contains("lore"))
            setLore(section.getStringList("lore"));

        if (section.contains("amount"))
            setAmount(section.getInt("amount"));

        if (section.contains("custom-model-data"))
            setCustomModelData(section.getInt("custom-model-data"));

        if (section.contains("unbreakable"))
            setUnbreakable(section.getBoolean("unbreakable"));

        if (section.contains("hide-flags") && section.getBoolean("hide-flags"))
            hideAllFlags();

        if (section.contains("glow") && section.getBoolean("glow"))
            setGlow(true);

        if (section.contains("enchantments")) {
            ConfigurationSection enchants = section.getConfigurationSection("enchantments");
            if (enchants != null) {
                for (String key : enchants.getKeys(false)) {
                    Enchantment enchantment = Enchantment.getByName(key.toUpperCase());
                    if (enchantment != null)
                        addEnchantment(enchantment, enchants.getInt(key));
                }
            }
        }
    }

    public ItemBuilder setDisplayName(String name) {
        meta.setDisplayName(color(name));
        return this;
    }

    public ItemBuilder setLore(List<String> lore) {
        meta.setLore(lore.stream().map(this::color).collect(Collectors.toList()));
        return this;
    }

    public ItemBuilder setLore(String... lines) {
        return setLore(List.of(lines));
    }

    public ItemBuilder setAmount(int amount) {
        item.setAmount(Math.max(1, Math.min(amount, 64)));
        return this;
    }

    public ItemBuilder setCustomModelData(int data) {
        meta.setCustomModelData(data);
        return this;
    }

    public ItemBuilder setUnbreakable(boolean unbreakable) {
        meta.setUnbreakable(unbreakable);
        return this;
    }

    public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
        meta.addEnchant(enchantment, level, true);
        return this;
    }

    public ItemBuilder removeEnchantment(Enchantment enchantment) {
        meta.removeEnchant(enchantment);
        return this;
    }

    public ItemBuilder addFlag(ItemFlag... flags) {
        meta.addItemFlags(flags);
        return this;
    }

    public ItemBuilder removeFlag(ItemFlag... flags) {
        meta.removeItemFlags(flags);
        return this;
    }

    public ItemBuilder hideAllFlags() {
        meta.addItemFlags(ItemFlag.values());
        return this;
    }

    public ItemBuilder setGlow(boolean glow) {
        if (glow) {
            meta.addEnchant(Enchantment.UNBREAKING, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        } else {
            meta.removeEnchant(Enchantment.UNBREAKING);
            meta.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        return this;
    }

    public ItemStack build() {
        item.setItemMeta(meta);
        return item;
    }

    private String color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}