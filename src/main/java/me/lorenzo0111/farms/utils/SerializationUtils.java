package me.lorenzo0111.farms.utils;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import me.lorenzo0111.farms.api.objects.Item;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class SerializationUtils {

    public static void item(FileConfiguration config, String path, ItemStack item) {
        item(config,path,new Item(item,-1,-1));
    }

    public static void item(FileConfiguration config, String path, Item item) {
        ItemMeta meta = item.getItem().getItemMeta();

        config.set(path+".type",item.getItem().getType().toString());
        config.set(path+".amount",item.getItem().getAmount());
        config.set(path+".name",meta == null || !meta.hasDisplayName() ? null : meta.getDisplayName().replace("§", "&"));

        if (item.getColumn() != -1)
            config.set(path+".column", item.getColumn());
        if (item.getRow() != -1)
            config.set(path+".row", item.getRow());

        if (meta == null || !meta.hasLore())
            return;

        List<String> newLore = new ArrayList<>();
        if (meta.getLore() != null) {
            for (String s : meta.getLore()) {
                newLore.add(s.replace("§", "&"));
            }
        }

        config.set(path+".lore",newLore);
    }

    public static Item item(FileConfiguration config, String path) {
        Material type = Material.valueOf(config.getString(path+".type"));
        int amount = config.getInt(path+".amount");
        String name = config.getString(path+".name");
        List<String> lore = null;
        if (config.contains(path+".lore"))
            lore = config.getStringList(path+".lore");

        ItemBuilder builder = ItemBuilder.from(type);
        if (amount > -1)
            builder.amount(amount);
        if (name != null)
            builder.name(Component.text(ChatColor.translateAlternateColorCodes('&', name)));
        if (lore != null) {
            List<Component> newLore = new ArrayList<>();
            for (String l : lore) {
                newLore.add(Component.text(ChatColor.translateAlternateColorCodes('&', l)));
            }
            builder.lore(newLore);
        }

        return new Item(builder.build(),config.getInt(path+".column"),config.getInt(path+".row"));
    }

    public static Item itemOr(FileConfiguration config, File file, String path, Item item) {
        if (!config.contains(path)) {
            item(config,path,item);
            try {
                config.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return item;
        }

        return item(config,path);
    }

}
