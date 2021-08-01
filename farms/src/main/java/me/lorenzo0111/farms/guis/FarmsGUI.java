package me.lorenzo0111.farms.guis;

import com.cryptomorin.xseries.XMaterial;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.components.InteractionModifier;
import dev.triumphteam.gui.guis.PaginatedGui;
import lombok.Getter;
import me.lorenzo0111.farms.Farms;
import me.lorenzo0111.farms.api.objects.Farm;
import me.lorenzo0111.farms.api.objects.Item;
import me.lorenzo0111.farms.utils.SerializationUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
public class FarmsGUI extends PaginatedGui {
    private final Farms plugin;
    private final Player author;

    public FarmsGUI(Farms plugin, Player author) {
        super(3, 17, ChatColor.translateAlternateColorCodes('&', plugin.getMessages().getString("commands.gui")), EnumSet.noneOf(InteractionModifier.class));

        this.plugin = plugin;
        this.author = author;
    }

    @Override
    public void open(@NotNull HumanEntity player) {
        this.setDefaultClickAction(e -> e.setCancelled(true));

        List<Farm> farms = plugin
                .getDataManager()
                .getFarms()
                .stream()
                .filter(farm -> player.hasPermission("farms.gui.other") || farm.getOwner().equals(player.getUniqueId()))
                .collect(Collectors.toList());

        Material material = XMaterial.END_ROD.parseMaterial();

        Objects.requireNonNull(material);

        Item item = SerializationUtils.itemOr(plugin.getGuiConfig(),
                plugin.getGuiFile(),
                "gui.farm",
                new Item(ItemBuilder.from(material)
                        .name(Component.text("§b§l[FARM]§e %name%"))
                        .lore(Component.text("§6Owner: §7%owner%"),
                                Component.text("§6Type: §7%type%"),
                                Component.empty(),
                                Component.text("§7§oClick to remove"))
                        .build(), -1, -1));

        for (Farm farm : farms) {
            ItemStack stack = item.getItem();
            ItemMeta meta = stack.getItemMeta();

            if (meta == null) continue;

            meta.setDisplayName(this.replace(meta.getDisplayName(), farm));
            List<String> lore = meta.getLore();

            if (lore != null) {
                for (int i = 0; i < lore.size(); i++) {
                    lore.set(i, this.replace(lore.get(i), farm));
                }
            }

            meta.setLore(lore);
            stack.setItemMeta(meta);

            this.addItem(ItemBuilder.from(stack).asGuiItem(e -> {
                if (farm.getOwner().equals(e.getWhoClicked().getUniqueId()) || e.getWhoClicked().hasPermission("farms.gui.other")) {
                    farm.destroy();
                    this.close(e.getWhoClicked());
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> this.open(e.getWhoClicked()), 20L);
                }
            }));
        }

        super.open(player);
    }

    private String replace(String text, Farm farm) {
        if (text == null) return null;

        String name = Bukkit.getOfflinePlayer(farm.getOwner()).getName();

        return text.replace("%name%", farm.getLocation().getX() + " " + farm.getLocation().getY() + " " + farm.getLocation().getZ())
                .replace("%owner%", name == null ? "Unknown" : name)
                .replace("%type%", farm.getBlock().name());
    }
}
