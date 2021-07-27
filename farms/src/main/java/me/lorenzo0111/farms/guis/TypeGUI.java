package me.lorenzo0111.farms.guis;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.components.InteractionModifier;
import dev.triumphteam.gui.guis.Gui;
import me.lorenzo0111.farms.Farms;
import me.lorenzo0111.farms.api.objects.Farm;
import me.lorenzo0111.farms.api.objects.Item;
import me.lorenzo0111.farms.utils.BlockUtils;
import me.lorenzo0111.farms.utils.SerializationUtils;
import me.lorenzo0111.farms.utils.TextUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;
import java.util.Objects;

public class TypeGUI extends Gui {
    private final Farm farm;
    private final Farms plugin;

    public TypeGUI(Farm farm, @NotNull Farms plugin, HumanEntity player) {
        super(1, TextUtils.text(plugin.getMessages(), "edit.type"), EnumSet.noneOf(InteractionModifier.class));

        this.farm = farm;
        this.plugin = plugin;
    }

    @Override
    public void open(@NotNull HumanEntity player) {
        Item filler = SerializationUtils.item(plugin.getGuiConfig(), "filler");

        Item info = SerializationUtils.itemOr(plugin.getGuiConfig(), plugin.getGuiFile(), "type.info", new Item(
                ItemBuilder.from(Material.END_ROD)
                        .name(Component.text("§7Drag your item"))
                        .lore(Component.text("§7Drag your item inside the empty slot"), Component.text("§7Then close the gui with the §e§nESC§7 button."))
                        .build(),
                1,
                1
        ));

        this.setCloseGuiAction(e -> {
            ItemStack item = e.getInventory().getItem(4);
            if (item == null || item.getType().equals(Material.AIR))
                return;

            ConfigurationSection section = Objects.requireNonNull(plugin.getConfig().getConfigurationSection("items"));

            if (!section.contains(item.getType().toString())) {
                e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessages().getString("prefix") + plugin.getMessages().getString("edit.no-type")));
                return;
            }

            final Material old = farm.getBlock();

            Material newType = Material.getMaterial(section.getString(item.getType().toString(), item.getType().toString()));
            farm.setBlock(newType);
            BlockUtils.full(farm, old);
        });

        this.getFiller().fill(ItemBuilder.from(filler.getItem()).asGuiItem(e -> e.setCancelled(true)));
        this.setItem(4, ItemBuilder.from(Material.AIR).asGuiItem());
        this.setItem(info.getRow(), info.getColumn(), ItemBuilder.from(info.getItem()).asGuiItem(e -> e.setCancelled(true)));

        super.open(player);
    }
}
