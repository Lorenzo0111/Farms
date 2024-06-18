/*
 * -------------------------------------
 * Copyright Lorenzo0111 2024
 * https://github.com/Lorenzo0111
 * -------------------------------------
 */

package me.lorenzo0111.farms.commands.subcommands;

import com.cryptomorin.xseries.XMaterial;
import de.tr7zw.changeme.nbtapi.NBT;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import lombok.Getter;
import me.lorenzo0111.farms.Farms;
import me.lorenzo0111.farms.api.objects.FarmType;
import me.lorenzo0111.farms.commands.FarmsCommand;
import me.lorenzo0111.farms.commands.SubCommand;
import me.lorenzo0111.farms.hooks.VaultHook;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CreateCommand extends SubCommand {
    @Getter private static final Material MATERIAL =
            XMaterial.END_ROD.parseMaterial() == null ?
                    Material.TORCH :
                    XMaterial.END_ROD.parseMaterial();

    public CreateCommand(FarmsCommand command) {
        super(command);
    }

    @Override
    public String[] getName() {
        return new String[]{"create", "setup", "buy"};
    }

    @Override
    public @Nullable String getPermission() {
        return "farms.create";
    }

    @Override
    public void execute(@NotNull Player player, String[] args) {
        FarmType type = FarmType.BLOCKS;
        if (args.length == 2) {
            try {
                type = FarmType.valueOf(args[1].toUpperCase());
            } catch (IllegalArgumentException ignored) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getCommand().getPlugin().getMessages().getString("prefix") + this.getCommand().getPlugin().getMessages().getString("commands.no-type")));
                return;
            }
        }

        if (!VaultHook.withdraw(player, getCommand().getPlugin().getConfig().getDouble("vault.prices." + type.name()))) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getCommand().getPlugin().getMessages().getString("prefix") + this.getCommand().getPlugin().getMessages().getString("commands.no-money")));
            return;
        }

        player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getCommand().getPlugin().getMessages().getString("prefix") + this.getCommand().getPlugin().getMessages().getString("commands.setup")));


        player.getInventory().addItem(getItem(type));
    }

    public static ItemStack getItem(FarmType type) {
        Component name = Component.text(ChatColor.translateAlternateColorCodes('&', "" + Farms.getInstance().getMessages().getString("setup.item-name")));
        Component lore = Component.text(ChatColor.translateAlternateColorCodes('&', "" + Farms.getInstance().getMessages().getString("setup.item-lore")));

        ItemStack item = ItemBuilder.from(MATERIAL)
                .name(name)
                .lore(lore)
                .build();

        NBT.modify(item, nbt -> {
            nbt.setString("custom_item", "farms");
            nbt.setInteger("farm_level", 1);
            nbt.setString("farm_type", type.name());
        });

        return item;
    }
}
