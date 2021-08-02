/*
 * -------------------------------------
 * Copyright Lorenzo0111 2021
 * https://github.com/Lorenzo0111
 * -------------------------------------
 */

package me.lorenzo0111.farms.guis;

import com.cryptomorin.xseries.XMaterial;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.components.InteractionModifier;
import dev.triumphteam.gui.guis.Gui;
import lombok.Getter;
import me.lorenzo0111.farms.Farms;
import me.lorenzo0111.farms.api.objects.Farm;
import me.lorenzo0111.farms.api.objects.Item;
import me.lorenzo0111.farms.collector.CollectorHandler;
import me.lorenzo0111.farms.utils.SerializationUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;
import java.util.List;

@Getter
public class FarmGUI extends Gui {
    private final Farms plugin;
    private final Player author;
    private final Farm farm;

    public FarmGUI(@NotNull Farms plugin, Player author, Farm farm) {
        super(5, ChatColor.translateAlternateColorCodes('&', plugin.getMessages().getString("edit.title") + ""), EnumSet.noneOf(InteractionModifier.class));

        this.plugin = plugin;
        this.author = author;
        this.farm = farm;
    }

    @Override
    public void open(@NotNull HumanEntity player) {
        this.setDefaultClickAction((e) -> e.setCancelled(true));

        if (XMaterial.GRAY_STAINED_GLASS_PANE.parseMaterial() == null)
            return;

        Item filler =  SerializationUtils.itemOr(
                plugin.getGuiConfig(),
                plugin.getGuiFile(),
                "filler",
                new Item(ItemBuilder.from(XMaterial.GRAY_STAINED_GLASS_PANE.parseMaterial())
                        .name(Component.text(""))
                        .build(), -1, -1)
        );

        Item delete =  SerializationUtils.itemOr(
                plugin.getGuiConfig(),
                plugin.getGuiFile(),
                "farm.delete",
                new Item(ItemBuilder.from(Material.BARRIER)
                        .name(Component.text("§7Pickup"))
                        .lore(Component.text("§7Click to §c§npickup§7 the farm."))
                        .build(), 5, 5)
        );

        Item levels =  SerializationUtils.itemOr(
                plugin.getGuiConfig(),
                plugin.getGuiFile(),
                "farm.levels",
                new Item(ItemBuilder.from(Material.NAME_TAG)
                        .name(Component.text("§7Levels"))
                        .lore(Component.text("§7Click to §e§nedit§7 the farm level."))
                        .build(), 3, 1)
        );

        Item type =  SerializationUtils.itemOr(
                plugin.getGuiConfig(),
                plugin.getGuiFile(),
                "farm.type",
                new Item(ItemBuilder.from(Material.DIAMOND_AXE)
                        .name(Component.text("§7Type"))
                        .lore(Component.text("§7Click to §e§nedit§7 the farm type."))
                        .build(), 5, 1)
        );

        Item collector = SerializationUtils.itemOr(
                plugin.getGuiConfig(),
                plugin.getGuiFile(),
                "farm.collector",
                new Item(ItemBuilder.from(Material.HOPPER)
                        .name(Component.text("§7Collector"))
                        .lore(Component.text("§7Click to §e§nset§7 a chest as collector."), Component.text("§7If you want to unlink it, break the chest."))
                        .build(), 5, 3)
        );

        Item storage =  SerializationUtils.itemOr(
                plugin.getGuiConfig(),
                plugin.getGuiFile(),
                "farm.storage",
                new Item(ItemBuilder.from(Material.CHEST)
                        .name(Component.text("§7Storage"))
                        .lore(Component.text("§7Click to §e§ncollect§7 all farmed items."))
                        .build(), 7, 1)
        );

        this.getFiller().fill(ItemBuilder.from(filler.getItem()).asGuiItem());

        this.setItem(delete.getRow(),delete.getColumn(), ItemBuilder.from(delete.getItem())
                .asGuiItem(e -> {
                    farm.pickup(e.getWhoClicked());
                    farm.destroy();
                    this.close(e.getWhoClicked());
                })
        );

        this.setItem(levels.getRow(),levels.getColumn(), ItemBuilder.from(levels.getItem()).asGuiItem(e -> new LevelGUI(this).open(e.getWhoClicked())));
        this.setItem(storage.getRow(),storage.getColumn(), ItemBuilder.from(storage.getItem()).asGuiItem(e -> {
            e.getWhoClicked().closeInventory();
            final List<ItemStack> items = farm.getItems();
            for (ItemStack item : items) {
                e.getWhoClicked().getInventory().addItem(item);
            }
            farm.getItems().clear();
        }));
        this.setItem(type.getRow(),type.getColumn(), ItemBuilder.from(type.getItem()).asGuiItem(e -> {
            e.getWhoClicked().closeInventory();
            new TypeGUI(farm,plugin).open(player);
        }));
        this.setItem(collector.getRow(),collector.getColumn(), ItemBuilder.from(collector.getItem()).asGuiItem(e -> {
            e.getWhoClicked().closeInventory();
            CollectorHandler.addMember((Player) e.getWhoClicked(), farm);
        }));

        super.open(player);
    }
}
