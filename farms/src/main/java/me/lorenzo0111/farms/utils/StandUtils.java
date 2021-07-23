package me.lorenzo0111.farms.utils;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.util.EulerAngle;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public final class StandUtils {

    public static ArmorStand miner(Location location, Player player, String name) {
        ArmorStand stand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        stand.setVisible(true);
        stand.setSmall(true);
        stand.setArms(true);
        stand.setGravity(false);
        stand.setInvulnerable(true);
        stand.setCustomName(name);
        stand.setRightArmPose(new EulerAngle(319f,0,0));
        stand.getEquipment().setItemInMainHand(ItemBuilder.from(Material.DIAMOND_PICKAXE)
                .glow()
                .build());
        stand.getEquipment().setItem(EquipmentSlot.FEET, ItemBuilder.from(Material.LEATHER_BOOTS).glow().build());
        stand.getEquipment().setItem(EquipmentSlot.LEGS, ItemBuilder.from(Material.LEATHER_LEGGINGS).glow().build());
        stand.getEquipment().setItem(EquipmentSlot.CHEST, ItemBuilder.from(Material.LEATHER_CHESTPLATE).glow().build());
        stand.getEquipment().setItem(EquipmentSlot.HEAD, ItemBuilder.skull().owner(player).glow().build());
        return stand;
    }

    public static @Nullable UUID farm(Entity entity) {
        if (entity.getCustomName() == null) return null;
        String name = entity.getCustomName();

        try {
            return UUID.fromString(name);
        } catch (IllegalArgumentException ignored) {return null; }
    }
}
