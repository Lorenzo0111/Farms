/*
 * -------------------------------------
 * Copyright Lorenzo0111 2022
 * https://github.com/Lorenzo0111
 * -------------------------------------
 */

package me.lorenzo0111.farms.utils;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import me.lorenzo0111.farms.Farms;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.EulerAngle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

public final class StandUtils {

    @SuppressWarnings("deprecation")
    public static ArmorStand miner(Location location, Player player, String name) {
        ArmorStand stand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        stand.setVisible(true);
        stand.setSmall(true);
        stand.setArms(true);
        stand.setGravity(false);

        try {
            stand.getClass().getMethod("setInvulnerable", boolean.class).invoke(stand,true);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ignored) {
            Farms.getInstance().debug("Unable to set invlunerable because of the not supported minecraft version. Trying to use anti-damage..");
        }

        stand.setCustomName(name);
        stand.setRightArmPose(new EulerAngle(319f,0,0));
        stand.getEquipment().setItemInHand(ItemBuilder.from(Material.DIAMOND_PICKAXE)
                .glow()
                .build());
        stand.getEquipment().setBoots(ItemBuilder.from(Material.LEATHER_BOOTS).glow().build());
        stand.getEquipment().setLeggings(ItemBuilder.from(Material.LEATHER_LEGGINGS).glow().build());
        stand.getEquipment().setChestplate(ItemBuilder.from(Material.LEATHER_CHESTPLATE).glow().build());
        stand.getEquipment().setHelmet(ItemBuilder.skull().owner(player).glow().build());
        return stand;
    }

    public static @Nullable UUID farm(@NotNull Entity entity) {
        if (entity.getCustomName() == null) return null;
        String name = entity.getCustomName();

        try {
            return UUID.fromString(name);
        } catch (IllegalArgumentException ignored) {return null; }
    }
}
