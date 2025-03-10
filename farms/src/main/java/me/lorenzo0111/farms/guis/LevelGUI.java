/*
 * -------------------------------------
 * Copyright Lorenzo0111 2025
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
import me.lorenzo0111.farms.api.objects.Item;
import me.lorenzo0111.farms.hooks.VaultHook;
import me.lorenzo0111.farms.utils.SerializationUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

@Getter
public class LevelGUI extends Gui {
    private final FarmGUI gui;
    private final Farms plugin;

    public LevelGUI(FarmGUI gui) {
        super(3, ChatColor.translateAlternateColorCodes('&', gui.getPlugin().getMessages().getString("edit.levels") + ""), EnumSet.noneOf(InteractionModifier.class));

        this.gui = gui;
        this.plugin = gui.getPlugin();
    }

    @Override
    public void open(@NotNull HumanEntity player) {
        if (XMaterial.GRAY_STAINED_GLASS_PANE.parseMaterial() == null)
            return;

        this.setDefaultClickAction((a) -> a.setCancelled(true));

        Item filler =  SerializationUtils.itemOr(
                plugin.getGuiConfig(),
                plugin.getGuiFile(),
                "filler",
                new Item(ItemBuilder.from(XMaterial.DIAMOND_AXE.parseMaterial())
                        .name(Component.text(""))
                        .build(), -1, -1)
        );

        Item levelOne =  SerializationUtils.itemOr(
                plugin.getGuiConfig(),
                plugin.getGuiFile(),
                "levels.one",
                new Item(ItemBuilder.from(XMaterial.DIAMOND_AXE.parseMaterial())
                        .name(Component.text("§eLevel One"))
                        .build(), 3, 2)
        );

        Item levelTwo =  SerializationUtils.itemOr(
                plugin.getGuiConfig(),
                plugin.getGuiFile(),
                "levels.two",
                new Item(ItemBuilder.from(XMaterial.DIAMOND_AXE.parseMaterial())
                        .name(Component.text("§eLevel Two"))
                        .build(), 4, 2)
        );

        Item levelThree =  SerializationUtils.itemOr(
                plugin.getGuiConfig(),
                plugin.getGuiFile(),
                "levels.three",
                new Item(ItemBuilder.from(XMaterial.DIAMOND_AXE.parseMaterial())
                        .name(Component.text("§eLevel Three"))
                        .build(), 5, 2)
        );

        Item levelFour =  SerializationUtils.itemOr(
                plugin.getGuiConfig(),
                plugin.getGuiFile(),
                "levels.four",
                new Item(ItemBuilder.from(XMaterial.DIAMOND_AXE.parseMaterial())
                        .name(Component.text("§eLevel Four"))
                        .build(), 6, 2)
        );

        Item levelFive =  SerializationUtils.itemOr(
                plugin.getGuiConfig(),
                plugin.getGuiFile(),
                "levels.five",
                new Item(ItemBuilder.from(XMaterial.DIAMOND_AXE.parseMaterial())
                        .name(Component.text("§eLevel Five"))
                        .build(), 7, 2)
        );

        this.setLevel(levelOne, 1);
        this.setLevel(levelTwo, 2);
        this.setLevel(levelThree, 3);
        this.setLevel(levelFour, 4);
        this.setLevel(levelFive, 5);

        this.getFiller().fill(ItemBuilder.from(filler.getItem())
                .asGuiItem());

        super.open(player);
    }

    private void setLevel(Item item, int level) {
        ItemBuilder builder = ItemBuilder.from(item.getItem());

        if (this.getGui().getFarm().getLevel() >= level){
            builder.glow(true);
        }

        this.setItem(item.getRow(),item.getColumn(), builder.asGuiItem(e -> {
            if (this.getGui().getFarm().getLevel() >= level) {
                e.getWhoClicked().sendMessage(ChatColor.translateAlternateColorCodes('&', this.getPlugin().getMessages().getString("prefix") + this.getPlugin().getMessages().getString("edit.unlocked")));
                return;
            }

            if (this.getGui().getFarm().getLevel() < level-1) {
                e.getWhoClicked().sendMessage(ChatColor.translateAlternateColorCodes('&', this.getPlugin().getMessages().getString("prefix") + this.getPlugin().getMessages().getString("edit.cannot")));
                return;
            }

            if (VaultHook.withdraw((Player) e.getWhoClicked(), plugin.getConfig().getInt("vault.levelUp") * level)) {
                this.getGui().getFarm().setLevel(level);
                this.open(e.getWhoClicked());
                return;
            }

            e.getWhoClicked().sendMessage(ChatColor.translateAlternateColorCodes('&', this.getPlugin().getMessages().getString("prefix") + this.getPlugin().getMessages().getString("edit.no-money")));
        }));
    }
}
